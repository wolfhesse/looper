package com.jdkanani.looper.route;

import com.jdkanani.looper.Request;
import com.jdkanani.looper.Response;

@FunctionalInterface
public interface FilterHandler {
	void handle(Request request, Response response, Chain chain);
}
