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
package org.hisp.dhis.android.core.arch.db.cursors.internal;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CursorExecutorImpl<M> implements CursorExecutor<M> {

    private final ObjectFactory<M> objectFactory;

    public CursorExecutorImpl(ObjectFactory<M> objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Override
    public void addObjectsToCollection(Cursor cursor, Collection<M> collection) {
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    collection.add(objectFactory.fromCursor(cursor));
                }
                while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public List<M> getObjects(Cursor cursor) {
        List<M> list = new ArrayList<>();
        addObjectsToCollection(cursor, list);
        return list;
    }
}
