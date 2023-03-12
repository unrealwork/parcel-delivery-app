package parcel.delivery.app;

record Person(String firstName, String lastName) {
    String fullName() {
        return firstName + " " + lastName;
    }
}
