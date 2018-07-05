Sample application presenting **Role Based Access Control**

User can perform patient, medical or admin role. Each role has a set of privileges. Only one role can be performed at a moment.  Application allows patients to make appointments for visits, medicals can schedule their duties and see registered patients. Administrator can change other user's role and edit personal data as well as remove account. Authorization is implemented using  http session. SSL connection provided.

Stack:
- Spring Web MVC
- Spring Data Jpa
- Spring Security
- MySQL
- Thymeleaf
- Bootstrap
