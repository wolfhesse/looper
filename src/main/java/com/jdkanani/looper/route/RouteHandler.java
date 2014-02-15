package com.jdkanani.looper.route;

import java.util.Objects;
import java.util.function.BiConsumer;

import com.jdkanani.looper.Request;
import com.jdkanani.looper.Response;

@FunctionalInterface
public interface RouteHandler {

	/**
	 * Handles incoming request with {@code request} and {@code response}.
	 *
	 * @param request Looper wrapper of incoming request
	 * @param response Looper wrapper of response
	 * @return
	 */
	Object handle(Request request, Response response);

	/**
	 * Returns a composed function which first applies {@code before} function,
	 * and then handle current request.

     * @param before the function to apply before request is handled.
     * @return a composed function that first applies the {@code before}
     * function and then handle request
     * @throws NullPointerException if before is null
	 */
	default RouteHandler compose(BiConsumer<Request, Response> before) {
        Objects.requireNonNull(before);
        return (request, response) -> {
    		before.accept(request, response);
    		return handle(request, response);
        };
    }

   /**
	* Returns a composed function that first handles request,
	* and then applies the {@code after} function.
    *
	* @param after
	* @return a composed function that first handles this function and then
    * applies the {@code after} function
    * @throws NullPointerException if after is null
	*/
    default RouteHandler andThen(BiConsumer<Request, Response> after) {
        Objects.requireNonNull(after);
        return (request, response) -> {
        	Object result = handle(request, response);
        	after.accept(request, response);
        	return result;
        };
    }
}
