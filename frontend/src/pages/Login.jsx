import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../services/auth.jsx';
import { loginUser } from '../services/api.js';
import { Navigate } from 'react-router-dom';
import { useState } from 'react';


export default function Login() {
    const navigate = useNavigate();
    const { authenticated, login} = useAuth();

    if(authenticated){
        return <Navigate to="/main" />;
    }

    const [form, setForm] = useState({
        email: "",
        password: "",
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm({
            ...form,
            [name]: value,
        });
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        try{
            const response = await loginUser({
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
                    <h1 className="app-title">Log in to your account</h1>
                    <p className="app-description">
                        Don't have an account? <Link to="/register" className="link">Sign up</Link>
                    </p>
                </div>

                <form className="auth-form" onSubmit={handleSubmit}>
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
                        placeholder="Enter your password" 
                        className="input-field full-width" 
                        />
                    </div>

                    <button type="submit" className="submit-button">Log in</button>
                </form>

                <div className="divider">
                    <span>Or login with</span>
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