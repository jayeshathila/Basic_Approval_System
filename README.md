# Basic_Approval_System
This is basic backend (login UI + API) for approval system using Spring Security + Mongo. I have used Doctor,Pharmasict and Patient system for prescription sharing demonstration.

## System Requirements:
1. Java
2. Gradle
3. Mongo (Replace mongo's config with yours in application.properties)

The system have five main packages

1. Controller: Which receives API requests and call appropriate action.
2. Repositories: Classes which implements MongoRepository and talks to mongo client using method definition.
3. Services: These are classes which are singleton objects initialzed and managed by Spring, the purpose of these
   classes is to perform actions and in cases to talk to mongo when queries get complicated in repositories.
4. Bean: These are data objects which can be used as DTO (data transfer object) or spring bean to be saved in mongo.
5. Utils: These are utility classes which enables developers to reuse utility methods.

## Functionalities:

1. User can login and logout. For testing purpose login will be used to get cookie which will be used in next Rest calls.

2. Doctors/Pharmasicts can get list of patients (only username and id).Patients cannot see each another's profile.

3. Doctors/Pharmasicts can get list of prescription names from patients username (no detail will be exposed yet).

4. Doctors/Pharmasicts can request for detailed prescrition using prescription title which they got from above.

5. Patient can approve pending request by login with their credentials.

6. Approved request will reflect the changes for Doctors/Pharmasicts.

## Testing

For testing purpose I have cleaned up and generated data on every deployement of application. After successull deployement 
developer is only needed to get cookies from localhost:8080 and use it in below provided APIS.

## APIS

1. 'GET' 'http://localhost:8080/user/patients' .   
   'Headers': `Content-Type: application/json
Cookie:hsfirstvisit=http%3A%2F%2Flocalhost%3A8081%2Fservice%2Foutreach%2F%3Fversion%3D3.0.0-03%26versionMm%3D3.0%26edition%3DOSS%26usertype%3Danonymous|http%3A%2F%2Flocalhost%3A8081%2F|1465048868186; __utma=111872281.1100900467.1465048867.1465048867.1465048867.1; __hstc=181257784.d21493976791f73d271f5897c3f4a53c.1465048868189.1465048868189.1465048868189.1; hubspotutk=d21493976791f73d271f5897c3f4a53c; SESSION=77e16ea6-375f-4634-aa96-c5a16d0ad078; JSESSIONID=B9DCC15EBEB01F4C7F4AEB26D193D44B`  

> Note replace Cookie with your own. I have skipped Headers section in below documentation, but in while using system you will have to add that in every call.  

This will list down all the registered patients. This will only work when you have logged in with Doctor or Pharmasict
credentials.

2. 'GET' 'http://localhost:8080/prescriptions?username=jayesh'  
This will list down all the prescriptions of user with username "jayesh".  

3. 'POST' 'http://localhost:8080/prescriptions/requestApproval/JAYESH_PRESCRIPTION_1' (No body required)  
This will send approval request to the patient.   

4. 'POST' 'http://localhost:8080/prescriptions/approve/JAYESH_PRESCRIPTION_1?usernameToApprove=doctor' (No body required)  
This will approve pending prescription detail with name "JAYESH_PRESCRIPTION_1" for doctor with username "doctor".  

5. 'GET' 'http://localhost:8080/prescriptions/details?prescriptionTitle=JAYESH_PRESCRIPTION_1'  
If the request is approved successfully this will return detailed prescription else it will throw unauthorized error.  
