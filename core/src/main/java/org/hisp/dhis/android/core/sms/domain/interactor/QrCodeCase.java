package org.hisp.dhis.android.core.sms.domain.interactor;

import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.sms.domain.converter.EventConverter;
import org.hisp.dhis.android.core.sms.domain.repository.LocalDbRepository;

import io.reactivex.Single;

public class QrCodeCase {
    private final LocalDbRepository localDbRepository;

    public QrCodeCase(LocalDbRepository localDbRepository) {
        this.localDbRepository = localDbRepository;
    }

    public Single<String> generateTextCode(final Event event) {
        return Single.zip(localDbRepository.getUserName(), localDbRepository.getDefaultCategoryOptionCombo(),
                (username, categoryOptionCombo) -> {
                    EventConverter converter = new EventConverter();
                    return converter.format(event, username, categoryOptionCombo);
                });
    }
}
