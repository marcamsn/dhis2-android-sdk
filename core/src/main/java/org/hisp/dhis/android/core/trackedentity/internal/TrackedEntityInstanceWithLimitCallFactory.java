/*
 * Copyright (c) 2004-2019, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.core.trackedentity.internal;

import org.hisp.dhis.android.core.arch.api.executors.internal.RxAPICallExecutor;
import org.hisp.dhis.android.core.arch.api.paging.internal.ApiPagingEngine;
import org.hisp.dhis.android.core.arch.api.paging.internal.Paging;
import org.hisp.dhis.android.core.arch.call.D2Progress;
import org.hisp.dhis.android.core.arch.call.internal.D2ProgressManager;
import org.hisp.dhis.android.core.arch.handlers.internal.Handler;
import org.hisp.dhis.android.core.arch.helpers.internal.BooleanWrapper;
import org.hisp.dhis.android.core.arch.repositories.collection.ReadOnlyWithDownloadObjectRepository;
import org.hisp.dhis.android.core.program.internal.ProgramDataDownloadParams;
import org.hisp.dhis.android.core.program.internal.ProgramOrganisationUnitLastUpdated;
import org.hisp.dhis.android.core.resource.internal.Resource;
import org.hisp.dhis.android.core.resource.internal.ResourceHandler;
import org.hisp.dhis.android.core.systeminfo.DHISVersionManager;
import org.hisp.dhis.android.core.systeminfo.SystemInfo;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance;
import org.hisp.dhis.android.core.user.internal.UserOrganisationUnitLinkStore;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import dagger.Reusable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Reusable
class TrackedEntityInstanceWithLimitCallFactory {

    private final Resource.Type resourceType = Resource.Type.TRACKED_ENTITY_INSTANCE;

    private final RxAPICallExecutor rxCallExecutor;
    private final ResourceHandler resourceHandler;
    private final Handler<ProgramOrganisationUnitLastUpdated> programOrganisationUnitLastUpdatedHandler;
    private final UserOrganisationUnitLinkStore userOrganisationUnitLinkStore;
    private final ReadOnlyWithDownloadObjectRepository<SystemInfo> systemInfoRepository;
    private final DHISVersionManager versionManager;
    private final TrackedEntityInstanceTrimmer trackedEntityInstanceTrimmer;

    private final TrackedEntityInstanceQueryBuilderFactory trackedEntityInstanceQueryBuilderFactory;

    private final TrackedEntityInstanceRelationshipDownloadAndPersistCallFactory relationshipDownloadCallFactory;
    private final TrackedEntityInstancePersistenceCallFactory persistenceCallFactory;
    private final TrackedEntityInstancesEndpointCallFactory endpointCallFactory;

    // TODO use scheduler for parallel download
    // private final Scheduler teiDownloadScheduler = Schedulers.from(Executors.newFixedThreadPool(6));

    @Inject
    TrackedEntityInstanceWithLimitCallFactory(
            RxAPICallExecutor rxCallExecutor,
            ResourceHandler resourceHandler,
            Handler<ProgramOrganisationUnitLastUpdated> programOrganisationUnitLastUpdatedHandler,
            UserOrganisationUnitLinkStore userOrganisationUnitLinkStore,
            ReadOnlyWithDownloadObjectRepository<SystemInfo> systemInfoRepository,
            TrackedEntityInstanceQueryBuilderFactory trackedEntityInstanceQueryBuilderFactory,
            TrackedEntityInstanceRelationshipDownloadAndPersistCallFactory relationshipDownloadCallFactory,
            TrackedEntityInstancePersistenceCallFactory persistenceCallFactory,
            DHISVersionManager versionManager,
            TrackedEntityInstanceTrimmer trackedEntityInstanceTrimmer,
            TrackedEntityInstancesEndpointCallFactory endpointCallFactory) {
        this.rxCallExecutor = rxCallExecutor;
        this.resourceHandler = resourceHandler;
        this.programOrganisationUnitLastUpdatedHandler = programOrganisationUnitLastUpdatedHandler;
        this.userOrganisationUnitLinkStore = userOrganisationUnitLinkStore;
        this.systemInfoRepository = systemInfoRepository;
        this.versionManager = versionManager;
        this.trackedEntityInstanceTrimmer = trackedEntityInstanceTrimmer;

        this.trackedEntityInstanceQueryBuilderFactory = trackedEntityInstanceQueryBuilderFactory;

        this.relationshipDownloadCallFactory = relationshipDownloadCallFactory;
        this.persistenceCallFactory = persistenceCallFactory;
        this.endpointCallFactory = endpointCallFactory;
    }

    Observable<D2Progress> download(final ProgramDataDownloadParams params) {
        Observable<D2Progress> observable = Observable.defer(() -> {
            D2ProgressManager progressManager = new D2ProgressManager(null);
            Set<ProgramOrganisationUnitLastUpdated> programOrganisationUnitSet = new HashSet<>();
            if (userOrganisationUnitLinkStore.count() == 0) {
                return Observable.just(
                        progressManager.increaseProgress(TrackedEntityInstance.class, true));
            } else {
                BooleanWrapper allOkay = new BooleanWrapper(true);

                return Observable.concatArray(
                        downloadSystemInfo(progressManager),
                        downloadTeis(progressManager, params, allOkay, programOrganisationUnitSet),
                        downloadRelationshipTeis(progressManager),
                        trimTrackedEntityInstances(progressManager, params),
                        updateResource(progressManager, params, allOkay, programOrganisationUnitSet)
                );
            }
        });

        return rxCallExecutor.wrapObservableTransactionally(observable, true);

    }

    private Observable<D2Progress> downloadSystemInfo(D2ProgressManager progressManager) {
        return systemInfoRepository.download()
                .toSingle(() -> progressManager.increaseProgress(SystemInfo.class, false))
                .toObservable();
    }

    private Observable<D2Progress> downloadTeis(D2ProgressManager progressManager,
                                                ProgramDataDownloadParams params,
                                                BooleanWrapper allOkay,
                                                Set<ProgramOrganisationUnitLastUpdated> programOrganisationUnitSet) {

        List<TeiQuery.Builder> teiQueryBuilders = trackedEntityInstanceQueryBuilderFactory.getTeiQueryBuilders(params);

        Observable<List<TrackedEntityInstance>> teiDownloadObservable =
                Observable.fromIterable(teiQueryBuilders)
                        .flatMap(teiQueryBuilder -> {
                            return getTrackedEntityInstancesWithPaging(teiQueryBuilder, allOkay);
                            // TODO .subscribeOn(teiDownloadScheduler);
                        });

        Date serverDate = systemInfoRepository.blockingGet().serverDate();

        boolean isFullUpdate = params.program() == null;
        boolean overwrite = params.overwrite();

        return teiDownloadObservable.flatMapSingle(
                teiList -> persistenceCallFactory.persistTEIs(teiList, isFullUpdate, overwrite)
                        .doOnComplete(() -> programOrganisationUnitSet.addAll(
                                TrackedEntityInstanceHelper.getProgramOrganisationUnitTuple(teiList, serverDate)))
                        .toSingle(() ->
                                progressManager.increaseProgress(TrackedEntityInstance.class, false)));
    }

    private Observable<D2Progress> downloadRelationshipTeis(D2ProgressManager progressManager) {
        Observable<List<TrackedEntityInstance>> observable = versionManager.is2_29()
                ? Observable.just(Collections.emptyList())
                : relationshipDownloadCallFactory.downloadAndPersist().toObservable();

        return observable.map(
                trackedEntityInstances -> progressManager.increaseProgress(TrackedEntityInstance.class, true));
    }

    private Observable<List<TrackedEntityInstance>> getTrackedEntityInstancesWithPaging(
            TeiQuery.Builder teiQueryBuilder, BooleanWrapper allOkay) {
        TeiQuery baseQuery = teiQueryBuilder.build();
        List<Paging> pagingList = ApiPagingEngine.getPaginationList(baseQuery.pageSize(), baseQuery.limit());

        return Observable
                .fromIterable(pagingList)
                .flatMapSingle(paging -> {
                    teiQueryBuilder.page(paging.page()).pageSize(paging.pageSize());
                    return endpointCallFactory.getCall(teiQueryBuilder.build())
                            .map(payload ->
                                new TeiListWithPaging(true, limitTeisForPage(payload.items(), paging), paging))
                            .onErrorResumeNext((err) -> {
                                allOkay.set(false);
                                return Single.just(new TeiListWithPaging(false, Collections.emptyList(), paging));
                            });
                })
                .takeUntil(res -> res.isSuccess && (res.paging.isLastPage() ||
                        !res.paging.isLastPage() && res.teiList.size() < res.paging.pageSize()))
                .map(tuple -> tuple.teiList);
    }

    private List<TrackedEntityInstance> limitTeisForPage(List<TrackedEntityInstance> pageTrackedEntityInstances,
                                                         Paging paging) {
        if (paging.isLastPage()
                && pageTrackedEntityInstances.size() > paging.previousItemsToSkipCount()) {
            int toIndex = Math.min(
                    pageTrackedEntityInstances.size(),
                    paging.pageSize() - paging.posteriorItemsToSkipCount());

            return pageTrackedEntityInstances.subList(paging.previousItemsToSkipCount(), toIndex);
        } else {
            return pageTrackedEntityInstances;
        }
    }

    private Observable<D2Progress> trimTrackedEntityInstances(D2ProgressManager progressManager,
                                                              ProgramDataDownloadParams params) {
        return Single.fromCallable(() -> {
            trackedEntityInstanceTrimmer.trimTrackedEntityInstances(params);
            return progressManager.increaseProgress(SystemInfo.class, false);
        }).toObservable();
    }

    private Observable<D2Progress> updateResource(D2ProgressManager progressManager,
                                                  ProgramDataDownloadParams params, BooleanWrapper allOkay,
                                                  Set<ProgramOrganisationUnitLastUpdated> programOrganisationUnitSet) {
        return Single.fromCallable(() -> {
            if (allOkay.get() && params.program() == null && params.orgUnits().isEmpty() && params.uids().isEmpty()) {
                resourceHandler.handleResource(resourceType);
            }
            programOrganisationUnitLastUpdatedHandler.handleMany(programOrganisationUnitSet);
            return progressManager.increaseProgress(TrackedEntityInstance.class, true);
        }).toObservable();
    }

    private static class TeiListWithPaging {
        final boolean isSuccess;
        final List<TrackedEntityInstance> teiList;
        final Paging paging;

        TeiListWithPaging(boolean isSuccess, List<TrackedEntityInstance> teiList, Paging paging) {
            this.isSuccess = isSuccess;
            this.teiList = teiList;
            this.paging = paging;
        }
    }
}