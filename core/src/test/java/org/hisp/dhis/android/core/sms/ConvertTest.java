
package org.hisp.dhis.android.core.sms;

import org.hisp.dhis.android.core.datavalue.DataValue;
import org.hisp.dhis.android.core.sms.domain.interactor.QrCodeCase;
import org.hisp.dhis.android.core.sms.domain.model.internal.SMSDataValueSet;
import org.hisp.dhis.android.core.sms.mockrepos.MockLocalDbRepository;
import org.hisp.dhis.android.core.sms.mockrepos.testobjects.MockMetadata;
import org.hisp.dhis.android.core.sms.mockrepos.testobjects.MockObjects;
import org.hisp.dhis.android.core.systeminfo.DHISVersionManager;
import org.hisp.dhis.android.core.systeminfo.SMSVersion;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeValue;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValue;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.models.AggregateDatasetSMSSubmission;
import org.hisp.dhis.smscompression.models.DeleteSMSSubmission;
import org.hisp.dhis.smscompression.models.EnrollmentSMSSubmission;
import org.hisp.dhis.smscompression.models.RelationshipSMSSubmission;
import org.hisp.dhis.smscompression.models.SMSAttributeValue;
import org.hisp.dhis.smscompression.models.SMSDataValue;
import org.hisp.dhis.smscompression.models.SMSEvent;
import org.hisp.dhis.smscompression.models.SMSSubmission;
import org.hisp.dhis.smscompression.models.SimpleEventSMSSubmission;
import org.hisp.dhis.smscompression.models.TrackerEventSMSSubmission;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Single;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ConvertTest {
    private MockLocalDbRepository testLocalDb;

    @Mock
    private DHISVersionManager dhisVersionManager;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        testLocalDb = new MockLocalDbRepository();
        when(dhisVersionManager.getSmsVersion()).thenReturn(SMSVersion.V2);
    }

    @Test
    public void convertEnrollment() throws Exception {
        EnrollmentSMSSubmission s = (EnrollmentSMSSubmission) convert(new QrCodeCase(testLocalDb, dhisVersionManager)
                .generateEnrollmentCode(MockObjects.enrollmentUid));
        assertEquals(s.getUserID().uid, MockObjects.user);
        assertEquals(s.getEnrollment().uid, MockObjects.enrollmentUid);
        assertEquals(s.getEnrollmentDate(), MockObjects.enrollmentDate);
        assertEquals(s.getEnrollmentStatus().name(), MockObjects.enrollmentStatus.name());
        assertEquals(s.getIncidentDate(), MockObjects.incidentDate);
        assertEquals(s.getOrgUnit().uid, MockObjects.orgUnit);
        assertEquals(s.getTrackerProgram().uid, MockObjects.program);
        assertEquals(s.getTrackedEntityType().uid, MockObjects.trackedEntityType);
        assertEquals(s.getTrackedEntityInstance().uid, MockObjects.teiUid);
        assertEquals(s.getCoordinates().getLatitude(), MockObjects.latitude, 0.0001);
        assertEquals(s.getCoordinates().getLongitude(), MockObjects.longitude, 0.0001);

        for (SMSAttributeValue item : s.getValues()) {
            assertTrue(containsAttributeValue(MockObjects.getTestAttributeValues(), item));
        }

        assertEquals(s.getEvents().size(), 1);
        SMSEvent event = s.getEvents().get(0);
        assertEquals(event.getEvent().uid, MockObjects.eventUid);
        assertEquals(event.getAttributeOptionCombo().uid, MockObjects.attributeOptionCombo);
        assertEquals(event.getEventDate(), MockObjects.eventDate);
        assertEquals(event.getDueDate(), MockObjects.dueDate);
        assertEquals(event.getEventStatus().name(), MockObjects.eventStatus.name());
        assertEquals(event.getOrgUnit().uid, MockObjects.orgUnit);
        for (SMSDataValue item : event.getValues()) {
            assertTrue(containsTeiDataValue(MockObjects.getTeiDataValues(), item));
        }
    }

    @Test
    public void convertEnrollmentWitNullEvent() throws Exception {
        EnrollmentSMSSubmission s = (EnrollmentSMSSubmission) convert(new QrCodeCase(testLocalDb, dhisVersionManager)
                .generateEnrollmentCode(MockObjects.enrollmentUidWithNullEvents));
        assertNull(s.getEvents());
    }

    @Test
    public void convertEnrollmentWithEmptyEventList() throws Exception {
        EnrollmentSMSSubmission s = (EnrollmentSMSSubmission) convert(new QrCodeCase(testLocalDb, dhisVersionManager)
                .generateEnrollmentCode(MockObjects.enrollmentUidWithoutEvents));
        assertNull(s.getEvents());
    }

    @Test
    public void convertEnrollmentWithoutGeometry() throws Exception {
        EnrollmentSMSSubmission s = (EnrollmentSMSSubmission) convert(new QrCodeCase(testLocalDb, dhisVersionManager)
                .generateEnrollmentCode(MockObjects.enrollmentUidWithoutGeometry));
        assertNull(s.getCoordinates());
    }

    @Test
    public void convertSimpleEvent() throws Exception {
        SimpleEventSMSSubmission s = (SimpleEventSMSSubmission) convert(new QrCodeCase(testLocalDb, dhisVersionManager)
                .generateSimpleEventCode(MockObjects.eventUid));
        assertEquals(s.getUserID().uid, MockObjects.user);
        assertEquals(s.getEvent().uid, MockObjects.eventUid);
        assertEquals(s.getEventDate(), MockObjects.eventDate);
        assertEquals(s.getEventStatus().name(), MockObjects.eventStatus.name());
        assertEquals(s.getEventProgram().uid, MockObjects.program);
        assertEquals(s.getDueDate(), MockObjects.dueDate);
        assertEquals(s.getAttributeOptionCombo().uid, MockObjects.attributeOptionCombo);
        assertEquals(s.getOrgUnit().uid, MockObjects.orgUnit);
        assertEquals(s.getCoordinates().getLatitude(), MockObjects.latitude, 0.0001);
        assertEquals(s.getCoordinates().getLongitude(), MockObjects.longitude, 0.0001);
        for (SMSDataValue item : s.getValues()) {
            assertTrue(containsTeiDataValue(MockObjects.getTeiDataValues(), item));
        }
    }

    @Test
    public void convertTrackerEvent() throws Exception {
        TrackerEventSMSSubmission s = (TrackerEventSMSSubmission) convert(new QrCodeCase(testLocalDb, dhisVersionManager)
                .generateTrackerEventCode(MockObjects.eventUid));
        assertEquals(s.getUserID().uid, MockObjects.user);
        assertEquals(s.getEvent().uid, MockObjects.eventUid);
        assertEquals(s.getEventDate(), MockObjects.eventDate);
        assertEquals(s.getEventStatus().name(), MockObjects.eventStatus.name());
        assertEquals(s.getProgramStage().uid, MockObjects.programStage);
        assertEquals(s.getDueDate(), MockObjects.dueDate);
        assertEquals(s.getAttributeOptionCombo().uid, MockObjects.attributeOptionCombo);
        assertEquals(s.getOrgUnit().uid, MockObjects.orgUnit);
        assertEquals(s.getEnrollment().uid, MockObjects.enrollmentUid);
        assertEquals(s.getCoordinates().getLatitude(), MockObjects.latitude, 0.0001);
        assertEquals(s.getCoordinates().getLongitude(), MockObjects.longitude, 0.0001);
        for (SMSDataValue item : s.getValues()) {
            assertTrue(containsTeiDataValue(MockObjects.getTeiDataValues(), item));
        }
    }

    @Test
    public void convertDataSet() throws Exception {
        AggregateDatasetSMSSubmission s = (AggregateDatasetSMSSubmission) convert(new QrCodeCase(testLocalDb, dhisVersionManager)
                .generateDataSetCode(MockObjects.dataSetUid, MockObjects.orgUnit,
                        MockObjects.period, MockObjects.attributeOptionCombo));
        assertEquals(s.getUserID().uid, MockObjects.user);
        assertEquals(s.getOrgUnit().uid, MockObjects.orgUnit);
        assertEquals(s.getAttributeOptionCombo().uid, MockObjects.attributeOptionCombo);
        assertEquals(s.getDataSet().uid, MockObjects.dataSetUid);
        assertEquals(s.getPeriod(), MockObjects.period);

        SMSDataValueSet mockSMSDataValueSet = MockObjects.getSMSDataValueSet();
        for (SMSDataValue item : s.getValues()) {
            assertTrue(containsDataValue(mockSMSDataValueSet.dataValues(), item));
        }
        assertEquals(s.isComplete(), MockObjects.isCompleted);
    }

    // TODO Enable this test when the compression supports empty lists
    //@Test
    public void convertDataSetWithEmptyDataValueList() throws Exception {
        AggregateDatasetSMSSubmission s = (AggregateDatasetSMSSubmission) convert(new QrCodeCase(testLocalDb, dhisVersionManager)
                .generateDataSetCode(MockObjects.dataSetEmptyListUid, MockObjects.orgUnit,
                        MockObjects.period, MockObjects.attributeOptionCombo));
        assertEquals(s.getUserID().uid, MockObjects.user);
        assertEquals(s.getOrgUnit().uid, MockObjects.orgUnit);
        assertEquals(s.getAttributeOptionCombo().uid, MockObjects.attributeOptionCombo);
        assertEquals(s.getDataSet().uid, MockObjects.dataSetUid);
        assertEquals(s.getPeriod(), MockObjects.period);

        assertEquals(s.getValues().size(), 0);
        assertEquals(s.isComplete(), MockObjects.isCompleted);
    }


    @Test
    public void convertRelationship() throws Exception {
        RelationshipSMSSubmission s = (RelationshipSMSSubmission) convert(new QrCodeCase(testLocalDb, dhisVersionManager)
                .generateRelationshipCode(MockObjects.relationship));
        assertEquals(s.getUserID().uid, MockObjects.user);
        assertEquals(s.getRelationship().uid, MockObjects.relationship);
        assertEquals(s.getFrom().uid, MockObjects.teiUid);
        assertEquals(s.getTo().uid, MockObjects.teiUid2);
        assertEquals(s.getRelationshipType().uid, MockObjects.relationshipType);
    }

    @Test
    public void convertDeletion() throws Exception {
        DeleteSMSSubmission s = (DeleteSMSSubmission) convert(new QrCodeCase(testLocalDb, dhisVersionManager)
                .generateDeletionCode(MockObjects.eventUid));
        assertEquals(s.getUserID().uid, MockObjects.user);
        assertEquals(s.getEvent().uid, MockObjects.eventUid);
    }

    private SMSSubmission convert(Single<String> task) throws Exception {
        AtomicReference<String> result = new AtomicReference<>();
        task.test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(value -> {
                    result.set(value);
                    return !value.isEmpty();
                });

        assertNotNull(result.get());
        byte[] smsBytes = Base64.getDecoder().decode(result.get());
        SMSSubmissionReader reader = new SMSSubmissionReader();
        SMSSubmission subm = reader.readSubmission(smsBytes, new MockMetadata());
        assertNotNull(subm);
        return subm;
    }

    private boolean containsTeiDataValue(ArrayList<TrackedEntityDataValue> values,
                                         SMSDataValue item) {
        for (TrackedEntityDataValue value : values) {
            if (Objects.equals(value.dataElement(), item.getDataElement().uid) &&
                    Objects.equals(value.value(), item.getValue())) {
                return true;
            }
        }
        return false;
    }

    private boolean containsAttributeValue(ArrayList<TrackedEntityAttributeValue> values,
                                           SMSAttributeValue item) {
        for (TrackedEntityAttributeValue value : values) {
            if (Objects.equals(value.trackedEntityAttribute(), item.getAttribute().uid) &&
                    Objects.equals(value.value(), item.getValue())) {
                return true;
            }
        }
        return false;
    }

    private boolean containsDataValue(Collection<DataValue> values, SMSDataValue item) {
        for (DataValue value : values) {
            if (Objects.equals(value.dataElement(), item.getDataElement().uid) &&
                    Objects.equals(value.value(), item.getValue())) {
                return true;
            }
        }
        return false;
    }
}
