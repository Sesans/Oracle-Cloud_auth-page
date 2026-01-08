import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../services/auth.jsx';
import { Navigate } from 'react-router-dom';
import { registerUser } from '../services/api.js';
import { useState } from 'react';

export default function Register() {
    const navigate = useNavigate();
    const { authenticated, login } = useAuth();

    if (authenticated) {
        return <Navigate to="/main" />;
    }

    const [form, setForm] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        confirmPassword: "",
        terms: false,
    });

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setForm({
        ...form,
        [name]: type === "checkbox" ? checked : value,
        });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (!form.terms) {
            alert("Você precisa aceitar os termos");
            return;
        }

        if (form.password !== form.confirmPassword) {
            alert("As senhas não coincidem");
            return;
        }

        try{
            const response = await registerUser({
                firstName: form.firstName,
                lastName: form.lastName,
                email: form.email,
                password: form.password,
            });
            login(response, response.token);
            navigate("/main", { replace: true });

        } catch (error) {
            console.error("Registration error:", error);
        }
    };

    return (
        <div className="app-container">
            <div className="image-section">
                <img src="/background.png" alt="Illustration" className="illustration-image" />
            </div>

            <div className="form-section">
                <div className="form-header">
                    <h1 className="app-title">Create an account</h1>
                    <p className="app-description">
                        Already have an account? <Link to="/login" className="link">Log in</Link>
                    </p>
                </div>

                <form className="auth-form" onSubmit={handleSubmit}>
                    <div className="input-group-row">
                        <input 
                        name="firstName"
                        value={form.firstName}
                        onChange={handleChange}
                        type="text" 
                        placeholder="First name" 
                        className="input-field" 
                        />
                        <input 
                        name="lastName"
                        value={form.lastName}
                        onChange={handleChange}
                        type="text" 
                        placeholder="Last name" 
                        className="input-field" 
                        />
                    </div>
                    
                    <input 
                    name="email"
                    value={form.email}
                    onChange={handleChange}
                    type="email" 
                    placeholder="Email" 
                    className="input-field full-width" 
                    />
                    
                    <div className="password-wrapper">
                        <input 
                        name="password"
                        value={form.password}
                        onChange={handleChange}
                        type="password" 
                        placeholder="Create your password" 
                        className="input-field full-width" 
                        />
                        <input 
                        name="confirmPassword"
                        value={form.confirmPassword}
                        onChange={handleChange}
                        type="password" 
                        placeholder="Confirm your password" 
                        className="input-field full-width" 
                        />
                    </div>

                    <div className="checkbox-container">
                        <input 
                        type="checkbox" 
                        name="terms"
                        checked={form.terms}
                        onChange={handleChange}
                        id="terms" 
                        />
                        <label htmlFor="terms">
                            I agree to the <span className="link-underlined">Terms & Conditions</span>
                        </label>
                    </div>

                    <button type="submit" className="submit-button">
                        Create account
                    </button>
                </form>

                <div className="divider">
                    <span>Or register with</span>
                </div>

                <div className="social-login">
                    <button className="social-button"
                        onClick={() => window.location.href = "http://localhost:8080/oauth2/authorization/google"}>

                        <img src="https://www.gstatic.com/images/branding/product/1x/gsa_512dp.png" alt="Google" /> Google

                    </button>
                    <button className="social-button">
                        <img src="https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png" alt="Github" /> Github
                    </button>
                </div>
            </div>
        </div>
    );
}