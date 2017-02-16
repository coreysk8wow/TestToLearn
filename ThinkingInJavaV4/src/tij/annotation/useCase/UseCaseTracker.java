package tij.annotation.useCase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Processor of Annotation UseCase
 *  
 * @author Liu Shun
 * Aug 22, 2013
 *
 */
public class UseCaseTracker {

	public static void trackUseCase(List<Integer> useCaseIds, Class klass) {
		UseCase uc;
		for (Method m : klass.getDeclaredMethods()) {
			 uc = m.getAnnotation(UseCase.class);
			 if (uc == null)
				 continue;
			 if(useCaseIds.contains(uc.id())) {
				 System.out.println("id = " + uc.id() + ", description = " + uc.description());
				 useCaseIds.remove(new Integer(uc.id()));
			 }
			 else {
				 System.out.println("Use case: " + uc.id() + " is not expected.");
			 }
		}
		
		for (int id : useCaseIds) {
			System.out.println("Missing use cases: " + id);
		}
	}
	
	public static void main(String[] args) {
		List<Integer> useCaseIds = new ArrayList<Integer>();
		Collections.addAll(useCaseIds, 1, 2, 3);
		trackUseCase(useCaseIds, PasswordUtils.class);
	}

}
