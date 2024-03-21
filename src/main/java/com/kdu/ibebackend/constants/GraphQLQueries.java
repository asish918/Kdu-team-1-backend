package com.kdu.ibebackend.constants;

/**
 * All constant queries are stored here
 */
public class GraphQLQueries {
    public static String testQuery = "{countRooms}";
    public static String fetchProperties = "{listProperties { property_name property_id}}";
    public static String basicNightlyRates = "{ listProperties (where: {property_id: {equals: 1}}){ room_type { room_rates { room_rate { basic_nightly_rate date room_rate_id } } } } }";
    public static String roomRes = "{ listRoomAvailabilities( where: {booking_id: {equals: 0}, date: {gte: \"2024-03-01T00:00:00.000Z\", lte: \"2024-03-02T00:00:00.000Z\"}, property_id: {equals: 1}} orderBy: {date: ASC}) { date room { room_id room_type_id room_type { area_in_square_feet double_bed max_capacity property_id room_type_name single_bed } } } }";
    public static String roomRateRoomTypeMappings = "{ listRoomRateRoomTypeMappings( where: {room_rate: {date: {gte: \"2024-03-01T00:00:00.000Z\", lte: \"2024-03-02T00:00:00.000Z\"}}, room_type: {property_id: {equals: 1}}} take: 1000000) { room_rate { basic_nightly_rate date } room_type { room_type_id room_type_name } } }";
}
