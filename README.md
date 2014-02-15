looper
======
Java 8 web framework

Quick start
-----------
-   Following code will start looper application at <http://localhost:5001>

```java
import static com.jdkanani.looper.Looper.*;
public class Main {
    public static void main(String[] args) {
		get("/", (request, response) -> "Hello world!");
		start();
    }
}
```

Examples
--------
**route parameters**

```java
import static com.jdkanani.looper.Looper.*;
public class Main {
    public static void main(String[] args) {
		get("/<user>", (request, response) -> {
			return request.params("user");
		});
		start();
    }
}
```

**using controller**
```java
// UserController.java
import com.jdkanani.looper.Request;
import com.jdkanani.looper.Response;

public class UserController {
	public static Object getUser(Request request, Response response) {
		return request.params("user");
	}
}

// Main.java
import static com.jdkanani.looper.Looper.*;
public class Main {
	public static void main(String[] args) {
        // Map controller method with route
		get("/<user>", UserController::getUser);
		start();
	}
}
```

**Filters**
```java
import static com.jdkanani.looper.Looper.*;
public static void main(String[] args) {
    filter("/secure", (request, response, chain) -> {
        if (request.getSession().getAttribute("authenticated") != null) {
            chain.next();
        } else {
            response.send(401, "You must login to access this url.");
        }
    });
    get("/secure", (request, response) -> "Your secure code is 1234");
    start();
}
```
