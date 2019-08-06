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

package org.hisp.dhis.android.core.program.internal;

import android.database.sqlite.SQLiteStatement;

import androidx.annotation.NonNull;

import org.hisp.dhis.android.core.arch.db.stores.binders.internal.IdentifiableStatementBinder;
import org.hisp.dhis.android.core.arch.db.stores.binders.internal.StatementBinder;
import org.hisp.dhis.android.core.arch.db.stores.internal.IdentifiableObjectStore;
import org.hisp.dhis.android.core.arch.db.stores.internal.StoreFactory;
import org.hisp.dhis.android.core.arch.db.stores.projections.internal.SingleParentChildProjection;
import org.hisp.dhis.android.core.arch.helpers.AccessHelper;
import org.hisp.dhis.android.core.arch.helpers.UidsHelper;
import org.hisp.dhis.android.core.data.database.DatabaseAdapter;
import org.hisp.dhis.android.core.program.ProgramStage;
import org.hisp.dhis.android.core.program.ProgramStageTableInfo;

import static org.hisp.dhis.android.core.arch.db.stores.internal.StoreUtils.sqLiteBind;

public final class ProgramStageStore {

    private static StatementBinder<ProgramStage> BINDER = new IdentifiableStatementBinder<ProgramStage>() {

        @Override
        public void bindToStatement(@NonNull ProgramStage o, @NonNull SQLiteStatement sqLiteStatement) {
            super.bindToStatement(o, sqLiteStatement);
            sqLiteBind(sqLiteStatement, 7, o.description());
            sqLiteBind(sqLiteStatement, 8, o.displayDescription());
            sqLiteBind(sqLiteStatement, 9, o.executionDateLabel());
            sqLiteBind(sqLiteStatement, 10, o.allowGenerateNextVisit());
            sqLiteBind(sqLiteStatement, 11, o.validCompleteOnly());
            sqLiteBind(sqLiteStatement, 12, o.reportDateToUse());
            sqLiteBind(sqLiteStatement, 13, o.openAfterEnrollment());
            sqLiteBind(sqLiteStatement, 14, o.repeatable());
            sqLiteBind(sqLiteStatement, 15, o.formType().name());
            sqLiteBind(sqLiteStatement, 16, o.displayGenerateEventBox());
            sqLiteBind(sqLiteStatement, 17, o.generatedByEnrollmentDate());
            sqLiteBind(sqLiteStatement, 18, o.autoGenerateEvent());
            sqLiteBind(sqLiteStatement, 19, o.sortOrder());
            sqLiteBind(sqLiteStatement, 20, o.hideDueDate());
            sqLiteBind(sqLiteStatement, 21, o.blockEntryForm());
            sqLiteBind(sqLiteStatement, 22, o.minDaysFromStart());
            sqLiteBind(sqLiteStatement, 23, o.standardInterval());
            sqLiteBind(sqLiteStatement, 24, UidsHelper.getUidOrNull(o.program()));
            sqLiteBind(sqLiteStatement, 25, o.periodType());
            sqLiteBind(sqLiteStatement, 26, AccessHelper.getAccessDataWrite(o.access()));
            sqLiteBind(sqLiteStatement, 27, o.remindCompleted());
            sqLiteBind(sqLiteStatement, 28, o.featureType());
        }
    };

    static final SingleParentChildProjection CHILD_PROJECTION = new SingleParentChildProjection(
            ProgramStageTableInfo.TABLE_INFO, ProgramStageFields.PROGRAM);

    private ProgramStageStore() {}

    public static IdentifiableObjectStore<ProgramStage> create(DatabaseAdapter databaseAdapter) {
        return StoreFactory.objectWithUidStore(databaseAdapter, ProgramStageTableInfo.TABLE_INFO,
                BINDER, ProgramStage::create);
    }
}