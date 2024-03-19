package com.kdu.ibebackend.constants;

/**
 * All constant queries are stored here
 */
public class GraphQLQueries {
    public static String testQuery = "{ countRooms }";
    public static String fetchProperties = "{listProperties { property_name property_id}}";
    public static String basicNightlyRates = "{ listProperties (where: {property_id: {equals: 1}}){ room_type { room_rates { room_rate { basic_nightly_rate date room_rate_id } } } } }";
}
