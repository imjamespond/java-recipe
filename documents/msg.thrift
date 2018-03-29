
namespace java com.metasoft.thrift

struct MsgStruct {
  1: string userid
  2: string msg
}

struct MsgToProcess {
  1: string admin
  2: string status
  3: string submituser
  4: string title
  5: string applid
}

struct MsgFinished {
  1: string user
  2: string title
  3: string applid
}

service MsgService {
  void send(1: MsgStruct msg)
  void sendMsgToProcess(1: MsgToProcess msg)
  void sendMsgFinished(1: MsgFinished msg)
}