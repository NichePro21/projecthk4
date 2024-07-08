
package ManagementSystem.fpt.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ApiController {

    public Map<String, String> parseFieldErrors(BindingResult rs) {
        List<FieldError> fieldErrors = rs.getFieldErrors();
        Map<String, String> errors = new HashMap();
        Iterator var4 = fieldErrors.iterator();

        while(var4.hasNext()) {
            FieldError error = (FieldError)var4.next();
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return errors;
    }
}
