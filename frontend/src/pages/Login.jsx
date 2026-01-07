import { Link } from 'react-router-dom';

export default function Login() {
    const handleSubmit = (event) => {
        event.preventDefault();
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
                    <input type="email" placeholder="Email" className="input-field full-width" />
                    
                    <div className="password-wrapper">
                        <input type="password" placeholder="Enter your password" className="input-field full-width" />
                    </div>

                    <button type="submit" className="submit-button">Log in</button>
                </form>

                <div className="divider">
                    <span>Or login with</span>
                </div>

                <div className="social-login">
                    <button className="social-button">
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