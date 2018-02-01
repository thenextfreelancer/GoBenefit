package com.gobenefit.web;

import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.User;

public class RequestScope {

	private static ThreadLocal<User> currentUser = new ThreadLocal<>();

	public static User getCurrentUser() {
		return currentUser.get();
	}

	public static void setCurrentUser(User user) {
		currentUser.set(user);
	}

	public static Apartment getCurrentApartment() {
		return currentUser.get().getCurrentApartment();
	}

	public static Long getCurrentApartmentId() {
		return currentUser.get().getCurrentApartment().getId();
	}

}
