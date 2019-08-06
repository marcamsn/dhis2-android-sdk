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

import android.content.ContentValues;
import android.database.Cursor;

import org.hisp.dhis.android.core.category.CategoryComboTableInfo;
import org.hisp.dhis.android.core.category.internal.CreateCategoryComboUtils;
import org.hisp.dhis.android.core.common.BaseIdentifiableObjectModel;
import org.hisp.dhis.android.core.common.BaseNameableObjectModel;
import org.hisp.dhis.android.core.legendset.LegendSetTableInfo;
import org.hisp.dhis.android.core.legendset.LegendTableInfo;
import org.hisp.dhis.android.core.program.Program;
import org.hisp.dhis.android.core.program.ProgramIndicatorTableInfo;
import org.hisp.dhis.android.core.program.ProgramRuleActionTableInfo;
import org.hisp.dhis.android.core.program.ProgramRuleTableInfo;
import org.hisp.dhis.android.core.program.ProgramRuleVariableTableInfo;
import org.hisp.dhis.android.core.program.ProgramTableInfo;
import org.hisp.dhis.android.core.program.ProgramTrackedEntityAttributeTableInfo;
import org.hisp.dhis.android.core.relationship.RelationshipTypeTableInfo;
import org.hisp.dhis.android.core.trackedentity.CreateTrackedEntityUtils;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeFields;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeTableInfo;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityTypeTableInfo;
import org.hisp.dhis.android.core.utils.integration.mock.BaseMockIntegrationTestEmptyEnqueable;
import org.hisp.dhis.android.core.utils.runner.D2JunitRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import static org.hisp.dhis.android.core.common.BaseIdentifiableObjectModel.Columns.UID;
import static org.hisp.dhis.android.core.data.database.CursorAssert.assertThatCursor;

@RunWith(D2JunitRunner.class)
public class ProgramEndpointCallMockIntegrationShould extends BaseMockIntegrationTestEmptyEnqueable {
    private static String ACCESS_DATA_WRITE = "accessDataWrite";
    private static String[] PROGRAM_PROJECTION = {
            UID,
            BaseIdentifiableObjectModel.Columns.CODE,
            BaseIdentifiableObjectModel.Columns.NAME,
            BaseIdentifiableObjectModel.Columns.DISPLAY_NAME,
            BaseIdentifiableObjectModel.Columns.CREATED,
            BaseIdentifiableObjectModel.Columns.LAST_UPDATED,
            BaseNameableObjectModel.Columns.SHORT_NAME,
            BaseNameableObjectModel.Columns.DISPLAY_SHORT_NAME,
            BaseNameableObjectModel.Columns.DESCRIPTION,
            BaseNameableObjectModel.Columns.DISPLAY_DESCRIPTION,
            ProgramFields.VERSION,
            ProgramFields.ONLY_ENROLL_ONCE,
            ProgramFields.ENROLLMENT_DATE_LABEL,
            ProgramFields.DISPLAY_INCIDENT_DATE,
            ProgramFields.INCIDENT_DATE_LABEL,
            ProgramFields.REGISTRATION,
            ProgramFields.SELECT_ENROLLMENT_DATES_IN_FUTURE,
            ProgramFields.DATA_ENTRY_METHOD,
            ProgramFields.IGNORE_OVERDUE_EVENTS,
            ProgramFields.SELECT_INCIDENT_DATES_IN_FUTURE,
            ProgramFields.USE_FIRST_STAGE_DURING_REGISTRATION,
            ProgramFields.DISPLAY_FRONT_PAGE_LIST,
            ProgramFields.PROGRAM_TYPE,
            ProgramFields.RELATED_PROGRAM,
            ProgramFields.TRACKED_ENTITY_TYPE,
            ProgramFields.CATEGORY_COMBO,
            ACCESS_DATA_WRITE,
            ProgramFields.ACCESS_LEVEL
    };

    private static Callable<List<Program>> programEndpointCall;

    @BeforeClass
    public static void setUpClass() throws Exception {
        BaseMockIntegrationTestEmptyEnqueable.setUpClass();

        ContentValues categoryCombo = CreateCategoryComboUtils.create(1L, "nM3u9s5a52V");
        database.insert(CategoryComboTableInfo.TABLE_INFO.name(), null, categoryCombo);

        ContentValues categoryCombo2 = CreateCategoryComboUtils.create(2L, "x31y45jvIQL");
        database.insert(CategoryComboTableInfo.TABLE_INFO.name(), null, categoryCombo2);

        // inserting tracked entity
        ContentValues trackedEntityType = CreateTrackedEntityUtils.create(1L, "nEenWmSyUEp");
        database.insert(TrackedEntityTypeTableInfo.TABLE_INFO.name(), null, trackedEntityType);

        programEndpointCall = objects.d2DIComponent.programCallFactory().create();
    }

    @Before
    public void setUp() throws IOException {
        dhis2MockServer.enqueueMockResponse("program/programs_complete.json");

    }

    @Test
    public void persist_program_when_call() throws Exception {
        // Fake call to api
        programEndpointCall.call();

        Cursor programCursor = database.query(ProgramTableInfo.TABLE_INFO.name(), PROGRAM_PROJECTION,
                null, null, null, null, null);

        assertThatCursor(programCursor).hasRow(
                "IpHINAT79UW",
                null,
                "Child Programme",
                "Child Programme",
                "2013-03-04T11:41:07.494",
                "2017-01-26T19:39:33.356",
                "Child Programme",
                "Child Programme",
                null,
                null,
                5,
                1, // true
                "Date of enrollment",
                1, // true
                "Date of birth",
                1, // true
                0, // false
                0, // false
                0, // false
                0, // false
                1, // true
                0, // false
                "WITH_REGISTRATION",
                null,
                "nEenWmSyUEp",
                "nM3u9s5a52V",
                0,
                "PROTECTED"
        ).isExhausted();
    }

    @Test
    public void persist_program_rule_variables_on_call() throws Exception {
        programEndpointCall.call();
        String[] projection = {
                UID,
                BaseIdentifiableObjectModel.Columns.CODE,
                BaseIdentifiableObjectModel.Columns.NAME,
                BaseIdentifiableObjectModel.Columns.DISPLAY_NAME,
                BaseIdentifiableObjectModel.Columns.CREATED,
                BaseIdentifiableObjectModel.Columns.LAST_UPDATED,
                ProgramRuleVariableFields.USE_CODE_FOR_OPTION_SET,
                ProgramRuleVariableFields.PROGRAM,
                ProgramRuleVariableFields.PROGRAM_STAGE,
                ProgramRuleVariableFields.DATA_ELEMENT,
                ProgramRuleVariableFields.TRACKED_ENTITY_ATTRIBUTE,
                ProgramRuleVariableFields.PROGRAM_RULE_VARIABLE_SOURCE_TYPE
        };

        Cursor programRuleVariableCursor = database.query(ProgramRuleVariableTableInfo.TABLE_INFO.name(), projection,
                UID + "=?", new String[]{"g2GooOydipB"}, null, null, null);

        assertThatCursor(programRuleVariableCursor).hasRow(
                "g2GooOydipB",
                null,
                "apgarscore",
                "apgarscore",
                "2015-08-07T18:41:55.152",
                "2015-08-07T18:41:55.153",
                null,
                "IpHINAT79UW",
                null,
                null,
                null,
                "DATAELEMENT_NEWEST_EVENT_PROGRAM"
        ).isExhausted();
    }

    @Test
    public void persist_program_tracker_entity_attributes_when_call() throws Exception {
        programEndpointCall.call();
        String[] projection = {
                UID,
                BaseIdentifiableObjectModel.Columns.CODE,
                BaseIdentifiableObjectModel.Columns.NAME,
                BaseIdentifiableObjectModel.Columns.DISPLAY_NAME,
                BaseIdentifiableObjectModel.Columns.CREATED,
                BaseIdentifiableObjectModel.Columns.LAST_UPDATED,
                BaseNameableObjectModel.Columns.SHORT_NAME,
                BaseNameableObjectModel.Columns.DISPLAY_SHORT_NAME,
                BaseNameableObjectModel.Columns.DESCRIPTION,
                BaseNameableObjectModel.Columns.DISPLAY_DESCRIPTION,
                ProgramTrackedEntityAttributeFields.MANDATORY,
                ProgramTrackedEntityAttributeFields.TRACKED_ENTITY_ATTRIBUTE,
                ProgramTrackedEntityAttributeFields.ALLOW_FUTURE_DATE,
                ProgramTrackedEntityAttributeFields.DISPLAY_IN_LIST,
                ProgramTrackedEntityAttributeFields.PROGRAM,
                ProgramTrackedEntityAttributeFields.SORT_ORDER
        };

        Cursor programTrackedEntityAttributeCursor = database.query(
                ProgramTrackedEntityAttributeTableInfo.TABLE_INFO.name(),
                projection,
                UID + "=?",
                new String[]{"l2T72XzBCLd"},
                null, null, null);

        assertThatCursor(programTrackedEntityAttributeCursor).hasRow(
                "l2T72XzBCLd",
                null,
                "Child Programme First name",
                "Child Programme First name",
                "2017-01-26T19:39:33.347",
                "2017-01-26T19:39:33.347",
                "Child Programme First name",
                "Child Programme First name",
                null,
                null,
                0, // false
                "w75KJ2mc4zz",
                0, // false
                1, // true
                "IpHINAT79UW",
                99
        ).isExhausted();
    }

    @Test
    public void persist_tracked_entity_attribute_when_call() throws Exception {
        programEndpointCall.call();
        String[] projection = {
                UID,
                BaseIdentifiableObjectModel.Columns.CODE,
                BaseIdentifiableObjectModel.Columns.NAME,
                BaseIdentifiableObjectModel.Columns.DISPLAY_NAME,
                BaseIdentifiableObjectModel.Columns.CREATED,
                BaseIdentifiableObjectModel.Columns.LAST_UPDATED,
                BaseNameableObjectModel.Columns.SHORT_NAME,
                BaseNameableObjectModel.Columns.DISPLAY_SHORT_NAME,
                BaseNameableObjectModel.Columns.DESCRIPTION,
                BaseNameableObjectModel.Columns.DISPLAY_DESCRIPTION,
                TrackedEntityAttributeFields.PATTERN,
                TrackedEntityAttributeFields.SORT_ORDER_IN_LIST_NO_PROGRAM,
                TrackedEntityAttributeFields.OPTION_SET,
                TrackedEntityAttributeFields.VALUE_TYPE,
                TrackedEntityAttributeFields.EXPRESSION,
                TrackedEntityAttributeFields.PROGRAM_SCOPE,
                TrackedEntityAttributeFields.DISPLAY_IN_LIST_NO_PROGRAM,
                TrackedEntityAttributeFields.GENERATED,
                TrackedEntityAttributeFields.DISPLAY_ON_VISIT_SCHEDULE,
                TrackedEntityAttributeFields.ORG_UNIT_SCOPE,
                TrackedEntityAttributeTableInfo.Columns.UNIQUE,
                TrackedEntityAttributeFields.INHERIT
        };

        Cursor trackedEntityAttributeCursor = database.query(TrackedEntityAttributeTableInfo.TABLE_INFO.name(),
                projection, UID + "=?", new String[]{"w75KJ2mc4zz"}, null, null, null);

        assertThatCursor(trackedEntityAttributeCursor).hasRow(
                "w75KJ2mc4zz",
                "MMD_PER_NAM",
                "First name",
                "First name",
                "2014-06-06T20:41:25.233",
                "2015-10-20T13:57:00.939",
                "First name",
                "First name",
                "First name",
                "First name",
                "",
                1,
                null,
                "TEXT",
                null,
                0, // false
                1, // true
                0, // false
                0, // false
                0, // false
                0, // false
                0 // false
        ).isExhausted();
    }

    @Test
    public void persist_program_indicators_when_call() throws Exception {
        programEndpointCall.call();

        Cursor programIndicatorCursor = database.query(
                ProgramIndicatorTableInfo.TABLE_INFO.name(),
                ProgramIndicatorTableInfo.TABLE_INFO.columns().all(),
                UID + "=?", new String[]{"rXoaHGAXWy9"}, null, null, null);
        assertThatCursor(programIndicatorCursor).hasRow(
                "rXoaHGAXWy9",
                null,
                "Health immunization score",
                "Health immunization score",
                "2015-10-20T11:26:19.631",
                "2015-10-20T11:26:19.631",
                "Health immunization score",
                "Health immunization score",
                "Sum of BCG doses, measles doses and yellow fever doses." +
                        " If Apgar score over or equal to 2, multiply by 2.",
                "Sum of BCG doses, measles doses and yellow fever doses." +
                        " If Apgar score over or equal to 2, multiply by 2.",
                0, // false
                "(#{A03MvHHogjR.bx6fsa0t90x} +  #{A03MvHHogjR.FqlgKAG8HOu} + #{A03MvHHogjR.rxBfISxXS2U}) " +
                        "* d2:condition('#{A03MvHHogjR.a3kGcGDCuk6} >= 2',1,2)",
                "rXoaHGAXWy9",
                null,
                2,
                "SUM",
                "IpHINAT79UW"
        ).isExhausted();
    }

    @Test
    public void persist_legend_sets_when_call() throws Exception {
        programEndpointCall.call();

        Cursor programIndicatorCursor = database.query(
                LegendSetTableInfo.TABLE_INFO.name(),
                LegendSetTableInfo.TABLE_INFO.columns().all(),
                UID + "=?", new String[]{"TiOkbpGEud4"}, null, null, null);
        assertThatCursor(programIndicatorCursor).hasRow(
                "TiOkbpGEud4",
                "AGE15YINT",
                "Age 15y interval",
                "Age 15y interval",
                "2017-06-02T11:40:33.452",
                "2017-06-02T11:41:01.999",
                "color"
        ).isExhausted();
    }

    @Test
    public void persist_legends_when_call() throws Exception {
        programEndpointCall.call();

        Cursor programIndicatorCursor = database.query(
                LegendTableInfo.TABLE_INFO.name(),
                LegendTableInfo.TABLE_INFO.columns().all(),
                UID + "=?", new String[]{"ZUUGJnvX40X"}, null, null, null);
        assertThatCursor(programIndicatorCursor).hasRow(
                "ZUUGJnvX40X",
                null,
                "30 - 40",
                "30 - 40",
                "2017-06-02T11:40:44.279",
                "2017-06-02T11:40:44.279",
                30.5,
                40,
                "#d9f0a3",
                "TiOkbpGEud4"
        ).isExhausted();
    }

    @Test
    public void persist_program_rules_when_call() throws Exception {
        programEndpointCall.call();
        String[] projection = {
                UID,
                BaseIdentifiableObjectModel.Columns.CODE,
                BaseIdentifiableObjectModel.Columns.NAME,
                BaseIdentifiableObjectModel.Columns.DISPLAY_NAME,
                BaseIdentifiableObjectModel.Columns.CREATED,
                BaseIdentifiableObjectModel.Columns.LAST_UPDATED,
                ProgramRuleFields.PRIORITY,
                ProgramRuleFields.CONDITION,
                ProgramRuleFields.PROGRAM,
                ProgramRuleFields.PROGRAM_STAGE
        };

        Cursor programRuleCursor = database.query(ProgramRuleTableInfo.TABLE_INFO.name(), projection,
                UID + "=?", new String[]{"NAgjOfWMXg6"}, null, null, null);

        assertThatCursor(programRuleCursor).hasRow(
                "NAgjOfWMXg6",
                null,
                "Ask for comment for low apgar",
                "Ask for comment for low apgar",
                "2015-09-14T21:17:40.841",
                "2015-09-14T22:22:15.383",
                null,
                "#{apgarscore} >= 0 && #{apgarscore} < 4 && #{apgarcomment} == ''",
                "IpHINAT79UW",
                null
        ).isExhausted();
    }

    @Test
    public void persist_program_rule_actions_when_call() throws Exception {
        programEndpointCall.call();

        String[] projection = {
                UID,
                ProgramRuleActionTableInfo.Columns.CODE,
                ProgramRuleActionTableInfo.Columns.NAME,
                ProgramRuleActionTableInfo.Columns.DISPLAY_NAME,
                ProgramRuleActionTableInfo.Columns.CREATED,
                ProgramRuleActionTableInfo.Columns.LAST_UPDATED,
                ProgramRuleActionFields.DATA,
                ProgramRuleActionFields.CONTENT,
                ProgramRuleActionFields.LOCATION,
                ProgramRuleActionFields.TRACKED_ENTITY_ATTRIBUTE,
                ProgramRuleActionFields.PROGRAM_INDICATOR,
                ProgramRuleActionFields.PROGRAM_STAGE_SECTION,
                ProgramRuleActionFields.PROGRAM_RULE_ACTION_TYPE,
                ProgramRuleActionFields.PROGRAM_STAGE,
                ProgramRuleActionFields.DATA_ELEMENT,
                ProgramRuleActionFields.PROGRAM_RULE
        };

        Cursor programRuleActionCursor = database.query(ProgramRuleActionTableInfo.TABLE_INFO.name(), projection,
                UID + "=?", new String[]{"v434s5YPDcP"}, null, null, null);

        assertThatCursor(programRuleActionCursor).hasRow(
                "v434s5YPDcP",
                null,
                null,
                null,
                "2015-09-14T21:17:41.033",
                "2015-09-14T22:22:15.458",
                null,
                "It is suggested that an explanation is provided when the Apgar score is below 4",
                null,
                null,
                null,
                null,
                "SHOWWARNING",
                null,
                null,
                "NAgjOfWMXg6"
        ).isExhausted();
    }

    /**
     * Relationship type doesn't exist for the program in the payload. Therefore we'll need to check that it doesn't
     * exist in the database
     *
     * @throws Exception
     */
    @Test
    public void not_persist_relationship_type_when_call() throws Exception {
        programEndpointCall.call();

        Cursor relationshipTypeCursor = database.query(RelationshipTypeTableInfo.TABLE_INFO.name(), RelationshipTypeTableInfo.TABLE_INFO.columns().all(),
                null, null, null, null, null);

        assertThatCursor(relationshipTypeCursor).isExhausted();
    }
}