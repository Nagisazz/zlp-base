export class Result {
  public static CANCEL = new Result({
    success:false,
    cancel:true,
  })
  public static Success(data?: any) {
    return new Result({
      success:true,
      data,
    })
  }
  public static Fail(errMsg: string) {
    return new Result({
      success:false,
      desc:errMsg,
    })
  }
  public success: boolean=false
  public cancel: boolean=false
  public desc: string=''
  public data: any
  constructor(data: any) {
    this.success = data.success||false
    this.cancel = data.cancel||false
    this.desc =data.desc ||''
    if(data.data === null || typeof data.data === 'undefined') {
      this.data = {}
    } else {
      this.data = data.data
    }
  }
}
