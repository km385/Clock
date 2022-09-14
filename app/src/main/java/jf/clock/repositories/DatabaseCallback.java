package jf.clock.repositories;

public interface DatabaseCallback<T>{
    void handleResponse(T response);
    void handleError(Exception e);
}
