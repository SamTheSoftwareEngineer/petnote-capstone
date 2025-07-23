
import "../css/NotFound.css";

function NotFoundPage() {
    return (
        <div className="not-found-container">
            <div className="not-found-box">
                <img
                    src="https://thumbs.dreamstime.com/b/dog-looking-bones-13187295.jpg"
                    alt="Dog searching for bone"
                    className="dog-image"
                />
                <h1>404 - Page Not Found</h1>
                <p>Uh oh! Looks like this pup can't find the page you're looking for.</p>
                <a href="/" className="home-button">Go Back Home</a>
            </div>
        </div>
    );
}

export default NotFoundPage;
