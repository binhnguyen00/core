import moment from "moment";

export class DateUtils {
  // these formats is to match the PSQL date format
  private static date = "YYYY-MM-DD";
  private static timestamp = "YYYY-MM-DD HH:mm:ss";
  private static timestampTz = "YYYY-MM-DD HH:mm:ssZ";
  // normal date format
  public static DATE = "DD-MM-YYYY";
  public static DATETIME = "DD-MM-YYYY HH:mm:ss";
  public static DATETIME_TZ = "DD-MM-YYYY HH:mm:ssZ";
  
  static getCurrentDate(format: string = this.date): string {
    return moment().format(format);
  }

  static subtractMonthsFromNow(month: number, format: string = this.date) {
    return moment().subtract(month, 'months').format(format);
  }

  static addMonthsToNow(month: number, format: string = this.date) {
    return moment().add(month, 'months').format(format);
  }

  static getTimestamp(format: string = this.timestamp): string {
    return moment().format(format);
  }

  static getTimestampTz(format: string = this.timestampTz): string {
    return moment().format(format);
  }

  static extractDate(target: string): string {
    let result = "";
    try {
      result = moment(target).format(this.date);
    } catch (error) {
      console.error(error);
    }
    return result;
  }

  static extractDateTime(target: string): string {
    let result = "";
    try {
      result = moment(target).format(this.timestamp);
    } catch (error) {
      console.error(error);
    }
    return result;
  }
}