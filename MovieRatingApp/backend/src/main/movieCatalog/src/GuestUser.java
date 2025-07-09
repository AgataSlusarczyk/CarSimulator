public class GuestUser implements User {
    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getDateOfBirth() {
        return "";
    }

    @Override
    public void addMovieToDatabase(Movie movie) {

    }

    @Override
    public boolean isAdmin() {
        return false;
    }
}
