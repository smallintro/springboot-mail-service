# spring boot email sender service


## Upload file for attachment
- Open http://localhost:8081/file and upload file

## Send mail
- Email client info is configured in application.properties file
- Trigger POST http://127.0.0.1:8081/mail/send
```
  {
  "subject": "[Do not reply]Sample email without attachment",
  "body": "Hi, This is sample mail without attachment",
  "to": ["example@example.com"],
  "cc": [],
  "bcc": [],
  "attachments": []
  }
 ```
 
- Email sender service will
  - validate the request and attachments.
  - compose the email with detail sent in request.
  - attach the file in mail.

## Email client app password
1. [Generate Gmail app password](https://support.google.com/accounts/answer/185833)
2. Login to [google account security](https://myaccount.google.com/security)
3. Enable Two-factor authentication.
4. Now, Go to App password > select Other > Give any name and click generate.
5. Copy the generated password and paste in application.properties as value of spring.mail.password