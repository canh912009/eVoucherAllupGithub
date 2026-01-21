import { Injectable } from '@nestjs/common';
import * as winston from 'winston';

@Injectable()
export class LoggerService {
  private readonly logger: winston.Logger;

  constructor() {
    this.logger = winston.createLogger({
      level: 'info',
      // level: 'debug',
      format: winston.format.combine(
        winston.format.timestamp(),
        winston.format.printf(
          ({ timestamp, level, message, context, service }) => {
            return `[${service}] ${
              process.pid
            } - ${timestamp} ${level.toUpperCase()} [${context}] ${message}`;
          },
        ),
        winston.format.colorize(),
      ),
      defaultMeta: { service: 'ev-file' },
      transports: [
        new winston.transports.File({
          filename: 'logs/app.log',
          maxsize: 10485760, // 10MB in bytes
          maxFiles: 10, // Maximum number of files to keep
        }),
        new winston.transports.File({
          filename: 'logs/error.log',
          level: 'error',
          maxsize: 10485760, // 10MB in bytes
          maxFiles: 10, // Maximum number of files to keep
        }),
        new winston.transports.Console(),
      ],
    });
  }

  private logMessage(
    level: string,
    message: any,
    ...optionalParams: any[]
  ): void {
    const context = optionalParams.pop() || '';

    if (typeof message === 'object') {
      message = JSON.stringify(message);
    }

    optionalParams = optionalParams.map((param) => {
      if (typeof param === 'object') {
        return JSON.stringify(param);
      }
      return param;
    });

    if (optionalParams.length > 0) {
      message = `${message} ${optionalParams.join(' ')}`;
    }

    this.logger.log(level, message, { context });
  }

  log(message: any, ...optionalParams: any[]): void {
    this.logMessage('info', message, ...optionalParams);
  }

  error(message: any, ...optionalParams: any[]): void {
    this.logMessage('error', message, ...optionalParams);
  }

  warn(message: any, ...optionalParams: any[]): void {
    this.logMessage('warn', message, ...optionalParams);
  }

  debug(message: any, ...optionalParams: any[]): void {
    this.logMessage('debug', message, ...optionalParams);
  }
}
