# grpc-java

## Protobuf version control

### Example
  
```text
// v1
  message Television {
    string brand = 1;
    int32 year = 2; -------- |
  }                          |
                             |
  |                          |
  |                          | tag matters!
  /                          |
                             |
// v2                        |
  message Television {       |
    string brand = 1;        |
    int32 modal = 2; ------- |
    Type type = 3;
  }
  enum Type {
    HD = 0;
    UHD = 2;
    OLED = 3;
 }
```


### Summary
  adding new fields will not break old proto
  removing fields will not break old proto when using reserved
  changing type
  int32 -> int64 ok
  int64 -> int32 might be problem
  renaming fields is ok


## RPC types
1. unary
2. server-stream
3. client-stream
4. bidirectional-stream

* Terminologies
  1. stub === receiver, or grpc client
