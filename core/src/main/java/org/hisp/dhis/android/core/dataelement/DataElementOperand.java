/*
 * Copyright (c) 2004-2018, University of Oslo
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

package org.hisp.dhis.android.core.dataelement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import org.hisp.dhis.android.core.common.BaseNameableObject;
import org.hisp.dhis.android.core.common.ObjectWithUid;
import org.hisp.dhis.android.core.data.api.Field;
import org.hisp.dhis.android.core.data.api.Fields;
import org.hisp.dhis.android.core.data.api.NestedField;

import java.util.Date;

import javax.annotation.Nullable;

@AutoValue
public abstract class DataElementOperand extends BaseNameableObject {

    private static final String DATA_ELEMENT = "dataElement";
    private static final String CATEGORY_OPTION_COMBO = "categoryOptionCombo";

    public static final Field<DataElementOperand, String> uid = Field.create(UID);
    private static final Field<DataElementOperand, String> code = Field.create(CODE);
    private static final Field<DataElementOperand, String> name = Field.create(NAME);
    private static final Field<DataElementOperand, String> displayName = Field.create(DISPLAY_NAME);
    private static final Field<DataElementOperand, String> created = Field.create(CREATED);
    private static final Field<DataElementOperand, String> lastUpdated = Field.create(LAST_UPDATED);
    private static final Field<DataElementOperand, String> shortName = Field.create(SHORT_NAME);
    private static final Field<DataElementOperand, String> displayShortName = Field.create(DISPLAY_SHORT_NAME);
    private static final Field<DataElementOperand, String> description = Field.create(DESCRIPTION);
    private static final Field<DataElementOperand, String> displayDescription = Field.create(DISPLAY_DESCRIPTION);
    private static final Field<DataElementOperand, Boolean> deleted = Field.create(DELETED);

    private static final NestedField<DataElementOperand, ObjectWithUid> dataElement
            = NestedField.create(DATA_ELEMENT);
    private static final NestedField<DataElementOperand, ObjectWithUid> categoryOptionCombo
            = NestedField.create(CATEGORY_OPTION_COMBO);


    public static final Fields<DataElementOperand> allFields = Fields.<DataElementOperand>builder().fields(
            uid, code, name, displayName, created, lastUpdated, shortName, displayShortName,
            description, displayDescription, deleted, dataElement.with(ObjectWithUid.uid),
            categoryOptionCombo.with(ObjectWithUid.uid)
            ).build();


    @Nullable
    @JsonProperty(DATA_ELEMENT)
    public abstract ObjectWithUid dataElement();

    String dataElementUid() {
        ObjectWithUid dataElement = dataElement();
        return dataElement == null ? "" : dataElement.uid();
    }

    @Nullable
    @JsonProperty(CATEGORY_OPTION_COMBO)
    public abstract ObjectWithUid categoryOptionCombo();

    String categoryOptionComboUid() {
        ObjectWithUid categoryOptionCombo = categoryOptionCombo();
        return categoryOptionCombo == null ? "" : categoryOptionCombo.uid();
    }

    @JsonCreator
    public static DataElementOperand create(
            @JsonProperty(UID) String uid,
            @JsonProperty(CODE) String code,
            @JsonProperty(NAME) String name,
            @JsonProperty(DISPLAY_NAME) String displayName,
            @JsonProperty(CREATED) Date created,
            @JsonProperty(LAST_UPDATED) Date lastUpdated,
            @JsonProperty(SHORT_NAME) String shortName,
            @JsonProperty(DISPLAY_SHORT_NAME) String displayShortName,
            @JsonProperty(DESCRIPTION) String description,
            @JsonProperty(DISPLAY_DESCRIPTION) String displayDescription,
            @JsonProperty(DELETED) Boolean deleted,
            @JsonProperty(DATA_ELEMENT) ObjectWithUid dataElement,
            @JsonProperty(CATEGORY_OPTION_COMBO) ObjectWithUid categoryOptionCombo) {

        return new AutoValue_DataElementOperand(uid, code, name,
                displayName, created, lastUpdated, deleted, shortName,
                displayShortName, description, displayDescription,
                dataElement, categoryOptionCombo);
    }

}