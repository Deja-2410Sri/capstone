import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';

export default function Register() {
  const [formData, setFormData] = useState({
    email: '', password: '', firstName: '', lastName: '', phone: ''
  });
  const [otp, setOtp] = useState('');
  const [verificationStep, setVerificationStep] = useState(false);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) =>
    setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    setLoading(true);

    try {
      const result = await register(formData);
      setVerificationStep(true);
      setMessage(result?.message || 'OTP sent. Check your email.');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed.');
    } finally {
      setLoading(false);
    }
  };

  const handleVerify = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    setLoading(true);

    try {
      const res = await authAPI.verifyOtp({ email: formData.email, otp });
      setMessage(res.data.message || 'Email verified successfully.');
      navigate('/login', {
        state: { message: 'Email verified! Please log in.' }
      });
    } catch (err) {
      setError(err.response?.data?.message || 'OTP verification failed.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6 col-lg-5">
          <div className="card shadow">
            <div className="card-body p-4">
              <h3 className="card-title text-center mb-4">
                {verificationStep ? 'Verify Your Email' : 'Register as Farmer'}
              </h3>

              {error && <div className="alert alert-danger">{error}</div>}
              {message && <div className="alert alert-success">{message}</div>}

              {!verificationStep ? (
                <form onSubmit={handleRegister}>
                  <div className="mb-3">
                    <label className="form-label">First Name</label>
                    <input type="text" className="form-control" name="firstName"
                      value={formData.firstName} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Last Name</label>
                    <input type="text" className="form-control" name="lastName"
                      value={formData.lastName} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Email</label>
                    <input type="email" className="form-control" name="email"
                      value={formData.email} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Phone</label>
                    <input type="tel" className="form-control" name="phone"
                      value={formData.phone} onChange={handleChange} />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Password</label>
                    <input type="password" className="form-control" name="password"
                      value={formData.password} onChange={handleChange}
                      required minLength={6} />
                  </div>
                  <button type="submit" className="btn btn-success w-100" disabled={loading}>
                    {loading ? 'Registering...' : 'Register'}
                  </button>
                </form>
              ) : (
                <form onSubmit={handleVerify}>
                  <p>Enter the 6-digit verification code sent to <strong>{formData.email}</strong>.</p>
                  <div className="mb-3">
                    <label className="form-label">Verification OTP</label>
                    <input type="text" className="form-control" inputMode="numeric"
                      autoComplete="one-time-code" maxLength={6} pattern="[0-9]{6}"
                      value={otp} onChange={(e) => setOtp(e.target.value)}
                      required placeholder="Enter 6-digit OTP" />
                  </div>
                  <button type="submit" className="btn btn-success w-100" disabled={loading}>
                    {loading ? 'Verifying...' : 'Verify Email'}
                  </button>
                </form>
              )}

              <p className="text-center mt-3">
                Already have an account? <Link to="/login">Login</Link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
