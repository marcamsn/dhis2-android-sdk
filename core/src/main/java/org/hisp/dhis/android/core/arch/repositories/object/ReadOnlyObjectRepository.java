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
package org.hisp.dhis.android.core.arch.repositories.object;

import org.hisp.dhis.android.core.common.CoreObject;

import io.reactivex.Single;

public interface ReadOnlyObjectRepository<M extends CoreObject> {

    /**
     * Returns the object in an asynchronous way, returning a {@link Single<M>}.
     * @return A {@link Single} object with the object
     */
    Single<M> get();

    /**
     * Returns the object in a synchronous way. Important: this is a blocking method and it should not be
     * executed in the main thread. Consider the asynchronous version {@link #get()}.
     * @return the object
     */
    M blockingGet();

    /**
     * Returns if the object exists in an asynchronous way, returning a {@link Single<Boolean>}.
     * @return if the object exists, wrapped in a {@link Single}
     */
    Single<Boolean> exists();

    /**
     * Returns if the object exists in a synchronous way. Important: this is a blocking method and it should not be
     * executed in the main thread. Consider the asynchronous version {@link #exists()}.
     * @return if the object exists
     */
    boolean blockingExists();
}