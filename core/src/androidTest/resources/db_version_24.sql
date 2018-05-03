CREATE TABLE UserRole (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT);
CREATE TABLE UserOrganisationUnit (_id INTEGER PRIMARY KEY AUTOINCREMENT,user TEXT NOT NULL,organisationUnit TEXT NOT NULL,organisationUnitScope TEXT NOT NULL, FOREIGN KEY (user)  REFERENCES User (uid)  ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (organisationUnit)  REFERENCES OrganisationUnit (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,UNIQUE (user, organisationUnit, organisationUnitScope));
CREATE TABLE UserCredentials (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,username TEXT,user TEXT NOT NULL UNIQUE, FOREIGN KEY (user) REFERENCES User (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED );
CREATE TABLE User (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,birthday TEXT,education TEXT,gender TEXT,jobTitle TEXT,surname TEXT,firstName TEXT,introduction TEXT,employer TEXT,interests TEXT,languages TEXT,email TEXT,phoneNumber TEXT,nationality TEXT);
CREATE TABLE TrackedEntityInstance (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,created TEXT, lastUpdated TEXT,createdAtClient TEXT,lastUpdatedAtClient TEXT,organisationUnit TEXT NOT NULL,trackedEntityType TEXT NOT NULL,coordinates TEXT,featureType TEXT,state TEXT, FOREIGN KEY (organisationUnit) REFERENCES OrganisationUnit (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED FOREIGN KEY (trackedEntityType) REFERENCES TrackedEntityType (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE TrackedEntityDataValue (_id INTEGER PRIMARY KEY AUTOINCREMENT,event TEXT NOT NULL,dataElement TEXT NOT NULL,storedBy TEXT,value TEXT,created TEXT,lastUpdated TEXT,providedElsewhere INTEGER, FOREIGN KEY (dataElement) REFERENCES DataElement (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,  FOREIGN KEY (event) REFERENCES Event (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE TrackedEntityAttributeValue (_id INTEGER PRIMARY KEY AUTOINCREMENT,created TEXT,lastUpdated TEXT,value TEXT,trackedEntityAttribute TEXT NOT NULL,trackedEntityInstance TEXT NOT NULL, FOREIGN KEY (trackedEntityAttribute) REFERENCES TrackedEntityAttribute (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,  FOREIGN KEY (trackedEntityInstance)  REFERENCES TrackedEntityInstance (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE TrackedEntityAttribute (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,shortName TEXT,displayShortName TEXT,description TEXT,displayDescription TEXT,pattern TEXT,sortOrderInListNoProgram INTEGER,optionSet TEXT,valueType TEXT,expression TEXT,searchScope TEXT,programScope INTEGER,displayInListNoProgram INTEGER,generated INTEGER,displayOnVisitSchedule INTEGER,orgunitScope INTEGER,uniqueProperty INTEGER,inherit INTEGER, FOREIGN KEY (optionSet) REFERENCES OptionSet (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE TrackedEntityType (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,shortName TEXT,displayShortName TEXT,description TEXT,displayDescription TEXT);
CREATE TABLE SystemInfo (_id INTEGER PRIMARY KEY AUTOINCREMENT, serverDate TEXT,dateFormat TEXT,version TEXT,contextPath TEXT);
CREATE TABLE Resource (_id INTEGER PRIMARY KEY AUTOINCREMENT,resourceType TEXT NOT NULL,lastSynced TEXT);
CREATE TABLE RelationshipType (_id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT NOT NULL UNIQUE, code TEXT, name TEXT, displayName TEXT, created TEXT, lastUpdated TEXT, bIsToA TEXT, AIsToB TEXT );
CREATE TABLE Relationship (_id INTEGER PRIMARY KEY AUTOINCREMENT,trackedEntityInstanceA TEXT NOT NULL,trackedEntityInstanceB TEXT NOT NULL,relationshipType TEXT NOT NULL, FOREIGN KEY (relationshipType)  REFERENCES RelationshipType (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED FOREIGN KEY (trackedEntityInstanceA)  REFERENCES TrackedEntityInstance (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED FOREIGN KEY (trackedEntityInstanceB)  REFERENCES TrackedEntityInstance (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE ProgramTrackedEntityAttribute (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,shortName TEXT,displayShortName TEXT,description TEXT,displayDescription TEXT,mandatory INTEGER,trackedEntityAttribute TEXT NOT NULL,allowFutureDate INTEGER,displayInList INTEGER,sortOrder INTEGER,program TEXT NOT NULL,searchable INTEGER, FOREIGN KEY (trackedEntityAttribute) REFERENCES TrackedEntityAttribute (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (program) REFERENCES Program (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE ProgramStageSectionProgramIndicatorLinkTable (_id INTEGER PRIMARY KEY AUTOINCREMENT,programStageSection TEXT NOT NULL,programIndicator TEXT NOT NULL, FOREIGN KEY (programStageSection)  REFERENCES ProgramStageSection (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (programIndicator)  REFERENCES ProgramIndicator (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,  UNIQUE (programStageSection, programIndicator));
CREATE TABLE ProgramStageSection (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,sortOrder INTEGER,programStage TEXT NOT NULL,desktopRenderType TEXT,mobileRenderType TEXT, FOREIGN KEY ( programStage) REFERENCES ProgramStage (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE ProgramStageDataElement (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,displayInReports INTEGER,compulsory INTEGER,allowProvidedElsewhere INTEGER,sortOrder INTEGER,allowFutureDate INTEGER,dataElement TEXT NOT NULL,programStage TEXT NOT NULL,programStageSection TEXT, FOREIGN KEY (programStage) REFERENCES ProgramStage (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (dataElement) REFERENCES DataElement (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (programStageSection) REFERENCES ProgramStageSection (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE ProgramRuleVariable (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,useCodeForOptionSet INTEGER,program TEXT NOT NULL,programStage TEXT,dataElement TEXT,trackedEntityAttribute TEXT,programRuleVariableSourceType TEXT, FOREIGN KEY (program) REFERENCES Program (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (programStage) REFERENCES ProgramStage (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (trackedEntityAttribute) REFERENCES TrackedEntityAttribute(uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (dataElement) REFERENCES DataElement (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE ProgramRuleAction (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,data TEXT,content TEXT,location TEXT,trackedEntityAttribute TEXT,programIndicator TEXT,programStageSection TEXT,programRuleActionType TEXT,programStage TEXT,dataElement TEXT,programRule TEXT NOT NULL, FOREIGN KEY (programRule) REFERENCES ProgramRule (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (trackedEntityAttribute) REFERENCES TrackedEntityAttribute (uid)  ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (programIndicator) REFERENCES ProgramIndicator (uid)  ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (programStageSection) REFERENCES ProgramStageSection (uid)  ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (programStage) REFERENCES ProgramStage (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (dataElement) REFERENCES DataElement (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE ProgramRule (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,priority INTEGER,condition TEXT,program TEXT NOT NULL,programStage TEXT, FOREIGN KEY (program) REFERENCES Program (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (programStage) REFERENCES ProgramStage (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE ProgramIndicator (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,shortName TEXT,displayShortName TEXT,description TEXT,displayDescription TEXT,displayInForm INTEGER,expression TEXT,dimensionItem TEXT,filter TEXT,decimals INTEGER,program TEXT NOT NULL, FOREIGN KEY (program) REFERENCES Program (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED );
CREATE TABLE Program (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,shortName TEXT,displayShortName TEXT,description TEXT,displayDescription TEXT,version INTEGER,onlyEnrollOnce INTEGER,enrollmentDateLabel TEXT,displayIncidentDate INTEGER,incidentDateLabel TEXT,registration INTEGER,selectEnrollmentDatesInFuture INTEGER,dataEntryMethod INTEGER,ignoreOverdueEvents INTEGER,relationshipFromA INTEGER,selectIncidentDatesInFuture INTEGER,captureCoordinates INTEGER,useFirstStageDuringRegistration INTEGER,displayFrontPageList INTEGER,programType TEXT,relationshipType TEXT,relationshipText TEXT,relatedProgram TEXT,trackedEntityType TEXT,categoryCombo TEXT, accessDataWrite INTEGER, expiryDays INTEGER, completeEventsExpiryDays INTEGER, expiryPeriodType TEXT, minAttributesRequiredToSearch INTEGER, maxTeiCountToReturn INTEGER, FOREIGN KEY (relationshipType) REFERENCES RelationshipType (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,  FOREIGN KEY (trackedEntityType) REFERENCES TrackedEntityType (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (categoryCombo) REFERENCES CategoryCombo (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE OrganisationUnitProgramLink (_id INTEGER PRIMARY KEY AUTOINCREMENT,organisationUnit TEXT NOT NULL,program TEXT NOT NULL, FOREIGN KEY (organisationUnit) REFERENCES OrganisationUnit (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (program) REFERENCES Program (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, UNIQUE (organisationUnit, program));
CREATE TABLE OrganisationUnit (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,shortName TEXT,displayShortName TEXT,description TEXT,displayDescription TEXT,path TEXT,openingDate TEXT,closedDate TEXT,level INTEGER,parent TEXT,displayNamePath TEXT);
CREATE TABLE OptionSet (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,version INTEGER,valueType TEXT);
CREATE TABLE Option (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,optionSet TEXT NOT NULL, FOREIGN KEY (optionSet)  REFERENCES OptionSet (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED );
CREATE TABLE Event (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,enrollment TEXT,created TEXT,lastUpdated TEXT,createdAtClient TEXT,lastUpdatedAtClient TEXT,status TEXT,latitude TEXT,longitude TEXT,program TEXT NOT NULL,programStage TEXT NOT NULL,organisationUnit TEXT NOT NULL,eventDate TEXT,completedDate TEXT,dueDate TEXT,state TEXT, attributeCategoryOptions TEXT, attributeOptionCombo TEXT, trackedEntityInstance TEXT, FOREIGN KEY (program) REFERENCES Program (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (programStage) REFERENCES ProgramStage (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,FOREIGN KEY (enrollment) REFERENCES Enrollment (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (organisationUnit) REFERENCES OrganisationUnit (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE Enrollment (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,created TEXT,lastUpdated TEXT,createdAtClient TEXT,lastUpdatedAtClient TEXT,organisationUnit TEXT NOT NULL,program TEXT NOT NULL,enrollmentDate TEXT,incidentDate TEXT,followup INTEGER,status TEXT,trackedEntityInstance TEXT NOT NULL,latitude TEXT,longitude TEXT,state TEXT, FOREIGN KEY (organisationUnit) REFERENCES OrganisationUnit (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (program) REFERENCES Program (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (trackedEntityInstance) REFERENCES TrackedEntityInstance (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE DataElement (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,shortName TEXT,displayShortName TEXT,description TEXT,displayDescription TEXT,valueType TEXT,zeroIsSignificant INTEGER,aggregationType TEXT,formName TEXT,numberType TEXT,domainType TEXT,dimension TEXT,displayFormName TEXT,optionSet TEXT,categoryCombo TEXT, FOREIGN KEY ( optionSet) REFERENCES OptionSet (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (categoryCombo) REFERENCES CategoryCombo (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE Constant (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,value TEXT);
CREATE TABLE Configuration (_id INTEGER PRIMARY KEY AUTOINCREMENT,serverUrl TEXT NOT NULL UNIQUE);
CREATE TABLE CategoryCategoryOptionLink (_id INTEGER PRIMARY KEY AUTOINCREMENT,category TEXT NOT NULL,categoryOption TEXT NOT NULL,  FOREIGN KEY (category)  REFERENCES Category (uid)  ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (categoryOption)  REFERENCES CategoryOption (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,UNIQUE (category, categoryOption));
CREATE TABLE CategoryOptionComboCategoryLink (_id INTEGER PRIMARY KEY AUTOINCREMENT,categoryOptionCombo TEXT NOT NULL,category TEXT NOT NULL,  FOREIGN KEY (categoryOptionCombo)  REFERENCES CategoryOptionCombo (uid)  ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (category)  REFERENCES CategoryOption (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,UNIQUE (categoryOptionCombo, category));
CREATE TABLE CategoryOptionCombo (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT, categoryCombo TEXT,  FOREIGN KEY (categoryCombo)  REFERENCES CategoryCombo (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED );
CREATE TABLE CategoryOption (_id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT NOT NULL UNIQUE, code TEXT, name TEXT, displayName TEXT, created TEXT, lastUpdated TEXT, shortName TEXT, displayShortName TEXT, description TEXT, displayDescription TEXT, startDate TEXT,endDate TEXT);
CREATE TABLE CategoryCategoryComboLink (_id INTEGER PRIMARY KEY AUTOINCREMENT,category TEXT NOT NULL,categoryCombo TEXT NOT NULL,  FOREIGN KEY (category)  REFERENCES Category (uid)  ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (categoryCombo)  REFERENCES CategoryCombo (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,UNIQUE (category, categoryCombo));
CREATE TABLE CategoryCombo (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT, isDefault INTEGER);
CREATE TABLE Category (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,dataDimensionType TEXT);
CREATE TABLE AuthenticatedUser (_id INTEGER PRIMARY KEY AUTOINCREMENT,user TEXT NOT NULL UNIQUE,credentials TEXT NOT NULL, FOREIGN KEY (user)  REFERENCES User (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE DataSet (_id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT NOT NULL UNIQUE, code TEXT, name TEXT, displayName TEXT, created TEXT, lastUpdated TEXT, shortName TEXT, displayShortName TEXT, description TEXT, displayDescription TEXT, periodType TEXT,categoryCombo TEXT NOT NULL,mobile INTEGER,version INTEGER,expiryDays INTEGER,timelyDays INTEGER,notifyCompletingUser INTEGER,openFuturePeriods INTEGER,fieldCombinationRequired INTEGER,validCompleteOnly INTEGER,noValueRequiresComment INTEGER,skipOffline INTEGER,dataElementDecoration INTEGER,renderAsTabs INTEGER,renderHorizontally INTEGER, accessDataWrite INTEGER, FOREIGN KEY ( categoryCombo) REFERENCES CategoryCombo (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE DataSetDataElementLink (_id INTEGER PRIMARY KEY AUTOINCREMENT, dataSet TEXT NOT NULL,dataElement TEXT NOT NULL, FOREIGN KEY (dataSet)  REFERENCES DataSet (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (dataElement)  REFERENCES DataElement (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, UNIQUE (dataSet, dataElement));
CREATE TABLE DataSetOrganisationUnitLink (_id INTEGER PRIMARY KEY AUTOINCREMENT, dataSet TEXT NOT NULL,organisationUnit TEXT NOT NULL, FOREIGN KEY (dataSet)  REFERENCES DataSet (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (organisationUnit)  REFERENCES OrganisationUnit (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, UNIQUE (dataSet, organisationUnit));
CREATE TABLE Indicator (_id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT NOT NULL UNIQUE, code TEXT, name TEXT, displayName TEXT, created TEXT, lastUpdated TEXT, shortName TEXT, displayShortName TEXT, description TEXT, displayDescription TEXT, annualized INTEGER,indicatorType TEXT,numerator TEXT,numeratorDescription TEXT,denominator TEXT,denominatorDescription TEXT,url TEXT, FOREIGN KEY ( indicatorType) REFERENCES IndicatorType (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE IndicatorType (_id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT NOT NULL UNIQUE, code TEXT, name TEXT, displayName TEXT, created TEXT, lastUpdated TEXT, shortName TEXT, displayShortName TEXT, description TEXT, displayDescription TEXT, number INTEGER,factor INTEGER);
CREATE TABLE DataSetIndicatorLink (_id INTEGER PRIMARY KEY AUTOINCREMENT, dataSet TEXT NOT NULL,indicator TEXT NOT NULL, FOREIGN KEY (dataSet)  REFERENCES DataSet (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (indicator)  REFERENCES Indicator (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, UNIQUE (dataSet, indicator));
CREATE TABLE DataValue (_id INTEGER PRIMARY KEY AUTOINCREMENT, dataElement TEXT NOT NULL,period TEXT NOT NULL,organisationUnit TEXT NOT NULL,categoryOptionCombo TEXT NOT NULL,attributeOptionCombo TEXT NOT NULL,value TEXT,storedBy TEXT,created TEXT,lastUpdated TEXT,comment TEXT,followUp INTEGER, FOREIGN KEY (dataElement)  REFERENCES DataElement (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (organisationUnit)  REFERENCES OrganisationUnit (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (categoryOptionCombo)  REFERENCES CategoryOptionCombo (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (attributeOptionCombo)  REFERENCES CategoryOptionCombo (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, UNIQUE (dataElement, period, organisationUnit, categoryOptionCombo, attributeOptionCombo));
CREATE TABLE Period (_id INTEGER PRIMARY KEY AUTOINCREMENT, periodId TEXT,periodType TEXT,startDate TEXT,endDate TEXT, UNIQUE (periodId));
CREATE TABLE ObjectStyle (_id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT,objectTable TEXT,color TEXT,icon TEXT, UNIQUE (uid));
CREATE TABLE ValueTypeDeviceRendering (_id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT,objectTable TEXT,deviceType TEXT,type TEXT,min INTEGER,max INTEGER,step INTEGER,decimalPoints INTEGER, UNIQUE (uid, deviceType));
CREATE TABLE Note (_id INTEGER PRIMARY KEY AUTOINCREMENT, enrollment TEXT,value TEXT,storedBy TEXT,storedDate TEXT, FOREIGN KEY (enrollment)  REFERENCES Enrollment (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, UNIQUE (enrollment, value, storedBy, storedDate));
CREATE TABLE ProgramStage (_id INTEGER PRIMARY KEY AUTOINCREMENT,uid TEXT NOT NULL UNIQUE,code TEXT,name TEXT,displayName TEXT,created TEXT,lastUpdated TEXT,executionDateLabel TEXT,allowGenerateNextVisit INTEGER,validCompleteOnly INTEGER,reportDateToUse TEXT,openAfterEnrollment INTEGER,repeatable INTEGER,captureCoordinates INTEGER,formType TEXT,displayGenerateEventBox INTEGER,generatedByEnrollmentDate INTEGER,autoGenerateEvent INTEGER,sortOrder INTEGER,hideDueDate INTEGER,blockEntryForm INTEGER,minDaysFromStart INTEGER,standardInterval INTEGER,program TEXT NOT NULL,periodType TEXT,accessDataWrite INTEGER, remindCompleted INTEGER, FOREIGN KEY ( program) REFERENCES Program (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE LegendSet (_id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT NOT NULL UNIQUE, code TEXT, name TEXT, displayName TEXT, created TEXT, lastUpdated TEXT, symbolizer TEXT);
CREATE TABLE Legend (_id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT NOT NULL UNIQUE, code TEXT, name TEXT, displayName TEXT, created TEXT, lastUpdated TEXT, startValue REAL,endValue REAL,color TEXT,legendSet TEXT, FOREIGN KEY ( legendSet) REFERENCES LegendSet (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED);
CREATE TABLE ProgramIndicatorLegendSetLink (_id INTEGER PRIMARY KEY AUTOINCREMENT, programIndicator TEXT NOT NULL,legendSet TEXT NOT NULL, FOREIGN KEY (programIndicator)  REFERENCES ProgramIndicator (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (legendSet)  REFERENCES LegendSet (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, UNIQUE (programIndicator, legendSet));
CREATE TABLE SystemSetting (_id INTEGER PRIMARY KEY AUTOINCREMENT, key TEXT,value TEXT, UNIQUE (key));
CREATE TABLE TrackedEntityAttributeReservedValue (_id INTEGER PRIMARY KEY AUTOINCREMENT, ownerObject TEXT,ownerUid TEXT,key TEXT,value TEXT,created TEXT,expiryDate TEXT,organisationUnit TEXT);