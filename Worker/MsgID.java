package Worker;

public class MsgID {
    public static final int REGISTER = 1;
    public static final int REGISTER_REPLY = 2;

    public static final int LOOKUP_SERVICE = 3;
    public static final int LOOKUP_SERVICE_REPLY = 4;

    public static final int INDEXING_SERVICE_REQUEST = 5;
    public static final int INDEXING_SERVICE_REPLY = 6;
    public static final int QUERY_SERVICE_REQUEST = 7;
    public static final int QUERY_SERVICE_REPLY = 8;
    
    public static final int MAPPER_TASK_REQUEST = 9;
    public static final int MAPPER_TASK_REPLY = 10;
    public static final int COMBINER_TASK_REQUEST = 11;
    public static final int COMBINER_TASK_REPLY = 12;
    public static final int REDUCER_TASK_REQUEST = 13;
    public static final int REDUCER_TASK_REPLY = 14;
    
}
