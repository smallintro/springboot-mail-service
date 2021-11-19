### spring boot email sender service


## Upload file for attachment
- Open http://localhost:8081/file and upload file

## Send mail
- Trigger POST http://127.0.0.1:8081/mail/send
  {
  "subject": "Test mail without attachment",
  "body": "Hi, I have received your mail with attachment",
  "to": ["cloudchain.ml@gmail.com"],
  "cc": [],
  "bcc": [],
  "attachments": []
  }
- Email sender service will
  - validate the request and attachments.
  - compose the email with detail sent in request.
  - attach the file in mail.
- Email client info is configured in application.properties file

## Email client app password
1. [Generate Gmail app password](https://support.google.com/accounts/answer/185833)
2. Login to google account
3. Go to security and enable Two-factor authentication.
4. Now, Go to App password > select Other > Give any name and click generate.
5. Copy the generated password and paste in application.properties as value of spring.mail.password