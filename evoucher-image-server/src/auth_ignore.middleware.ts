import { Logger, Injectable, NestMiddleware } from '@nestjs/common';
import { Request, Response, NextFunction } from 'express';

@Injectable()
export class AuthIgnoreMiddleware implements NestMiddleware {
  private readonly logger = new Logger(AuthIgnoreMiddleware.name);

  private TIME_OUT: number = Number.parseInt(
    process.env.REQUEST_DELAY_TIME || '0',
  );

  use(req: Request, res: Response, next: NextFunction) {
    // const now = new Date();
    // const formattedDateTime = now.toISOString().slice(0, 23).replace('T', ' ');
    // console.log(formattedDateTime, 'url', req.originalUrl);

    this.logger.log('url ' + req.originalUrl);

    try {
      if (this.TIME_OUT > 0) {
        setTimeout(() => {
          next();
        }, this.TIME_OUT);
      } else {
        next();
      }
    } catch (err) {
      return res.status(401).send({ message: 'Invalid or expired token' });
    }
  }
}
