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

package org.hisp.dhis.android.core.period.internal;

import org.hisp.dhis.android.core.arch.db.querybuilders.internal.WhereClauseBuilder;
import org.hisp.dhis.android.core.common.BaseIdentifiableObject;
import org.hisp.dhis.android.core.period.DatePeriod;

import java.util.List;

public final class InPeriodQueryHelper {

    private InPeriodQueryHelper(){
    }

    public static String buildInPeriodsQuery(String key, List<DatePeriod> datePeriods) {
        WhereClauseBuilder builder = new WhereClauseBuilder();
        for (int i = 0; i < datePeriods.size(); i++) {
            builder.appendComplexQuery(buildWhereClause(key, datePeriods, i));

            if (i != datePeriods.size() - 1) {
                builder
                        .appendOperator(" OR ");
            }
        }

        return builder.build();
    }

    private static String buildWhereClause(String key, List<DatePeriod> datePeriods, int i) {
        return new WhereClauseBuilder()
                .appendKeyGreaterOrEqStringValue(key,
                        BaseIdentifiableObject.DATE_FORMAT.format(datePeriods.get(i).startDate()))
                .appendKeyLessThanOrEqStringValue(key,
                        BaseIdentifiableObject.DATE_FORMAT.format(datePeriods.get(i).endDate()))
                .build();
    }
}