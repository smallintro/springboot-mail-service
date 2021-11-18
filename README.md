## spring boot email sender service

- Trigger a rest api with/without file to send mail.
- Email sender service will
  - will validate the rest request and attachments.
  - compose the email with detail sent via rest request.
  - attach the file in mail if uploaded along with the rest request.
- Email client info is configured in application.properties file

## Email client configuration
1. [Generate Gmail app password](https://support.google.com/accounts/answer/185833)