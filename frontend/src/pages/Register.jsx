import { Link } from 'react-router-dom';

export default function Register() {
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
                    <h1 className="app-title">Create an account</h1>
                    <p className="app-description">
                        Already have an account? <Link to="/login" className="link">Log in</Link>
                    </p>
                </div>

                <form className="auth-form" onSubmit={handleSubmit}>
                    <div className="input-group-row">
                        <input type="text" placeholder="First name" className="input-field" />
                        <input type="text" placeholder="Last name" className="input-field" />
                    </div>
                    
                    <input type="email" placeholder="Email" className="input-field full-width" />
                    
                    <div className="password-wrapper">
                        <input type="password" placeholder="Create your password" className="input-field full-width" />
                        <input type="password" placeholder="Confirm your password" className="input-field full-width" />
                    </div>

                    <div className="checkbox-container">
                        <input type="checkbox" id="terms" />
                        <label htmlFor="terms">I agree to the <span className="link-underlined">Terms & Conditions</span></label>
                    </div>

                    <button type="submit" className="submit-button">Create account</button>
                </form>

                <div className="divider">
                    <span>Or register with</span>
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