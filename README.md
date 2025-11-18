A simple backend project that allows users to register using their email and verify their account using a One-Time Password (OTP) sent via email.
This project is designed for Postman testing only (no frontend).

API Endpoints (For Postman Testing)

1. Register User + Send OTP
   
POST
http://localhost:8080/api/auth/register

Body:
{
  "email": "example@gmail.com",
  "password": "test123"
}

Response:
{
  "message": "User registered successfully. OTP sent to email."
}

2. Verify OTP
3. 
POST
http://localhost:8080/api/auth/verify-otp?email=example@gmail.com&otp=123456

Response:
{
  "message": "Email verified successfully!"
}
