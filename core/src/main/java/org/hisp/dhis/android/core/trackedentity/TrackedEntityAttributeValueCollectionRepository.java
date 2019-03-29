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

package org.hisp.dhis.android.core.trackedentity;

import org.hisp.dhis.android.core.arch.repositories.children.ChildrenAppender;
import org.hisp.dhis.android.core.arch.repositories.collection.ReadOnlyCollectionRepositoryImpl;
import org.hisp.dhis.android.core.arch.repositories.filters.DateFilterConnector;
import org.hisp.dhis.android.core.arch.repositories.filters.FilterConnectorFactory;
import org.hisp.dhis.android.core.arch.repositories.filters.StringFilterConnector;
import org.hisp.dhis.android.core.arch.repositories.scope.RepositoryScope;

import java.util.Map;

import javax.inject.Inject;

import dagger.Reusable;

@Reusable
public final class TrackedEntityAttributeValueCollectionRepository
        extends ReadOnlyCollectionRepositoryImpl<TrackedEntityAttributeValue,
        TrackedEntityAttributeValueCollectionRepository> {

    @Inject
    TrackedEntityAttributeValueCollectionRepository(
            final TrackedEntityAttributeValueStore store,
            final Map<String, ChildrenAppender<TrackedEntityAttributeValue>> childrenAppenders,
            final RepositoryScope scope) {
        super(store, childrenAppenders, scope, new FilterConnectorFactory<>(scope,
                s -> new TrackedEntityAttributeValueCollectionRepository(store, childrenAppenders, s)));
    }

    public StringFilterConnector<TrackedEntityAttributeValueCollectionRepository> byTrackedEntityAttribute() {
        return cf.string(TrackedEntityAttributeValueTableInfo.Columns.TRACKED_ENTITY_ATTRIBUTE);
    }

    public StringFilterConnector<TrackedEntityAttributeValueCollectionRepository> byValue() {
        return cf.string(TrackedEntityAttributeValueFields.VALUE);
    }

    public DateFilterConnector<TrackedEntityAttributeValueCollectionRepository> byCreated() {
        return cf.date(TrackedEntityAttributeValueFields.CREATED);
    }

    public DateFilterConnector<TrackedEntityAttributeValueCollectionRepository> byLastUpdated() {
        return cf.date(TrackedEntityAttributeValueFields.LAST_UPDATED);
    }

    public StringFilterConnector<TrackedEntityAttributeValueCollectionRepository> byTrackedEntityInstance() {
        return cf.string(TrackedEntityAttributeValueTableInfo.Columns.TRACKED_ENTITY_INSTANCE);
    }
}