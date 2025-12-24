public Map<String, Object> registerUser(String name, String email, String password, String role) {
    Map<String, Object> map = new HashMap<>();
    map.put("userId", new Random().nextLong(1000, 9999));
    map.put("name", name);
    map.put("email", email);
    map.put("password", password);
    map.put("role", role);
    users.put(email, password);
    roles.put(email, role);
    return map;
}
