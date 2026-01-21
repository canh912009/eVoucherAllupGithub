import { ForbiddenException, Injectable, Logger, NestMiddleware } from '@nestjs/common';
import { Request, Response, NextFunction } from 'express';
// import * as jwt from 'jsonwebtoken';
import { HttpService } from '@nestjs/axios';
import { API_ME } from './config/config';
import { map, catchError, lastValueFrom } from 'rxjs';

@Injectable()
export class AuthMiddleware implements NestMiddleware {
  
  private readonly logger = new Logger(AuthMiddleware.name);
  constructor(private http: HttpService) {}

  async getMeApiMessage(token: string) {
    const headers = {
      Authorization: `Bearer ${token}`,
    };
    const request = this.http
      .get(API_ME, { headers })
      .pipe(map((res) => res.data))
      .pipe(
        catchError(() => {
          throw new ForbiddenException('API not available');
        }),
      );

    return await lastValueFrom(request);
  }

  async use(req: Request, res: Response, next: NextFunction) {
    const authHeader = req.headers.authorization;

    // console.log(req.protocol + '://' + req.get('host') + req.originalUrl);
    this.logger.log('url', req.originalUrl);

    if (!authHeader) {
      return res.status(401).send({ message: 'Authorization header missing' });
    }

    const [bearer, token] = authHeader.split(' ');
    this.logger.log('token', token);

    if (bearer !== 'Bearer' || !token) {
      return res.status(401).send({ message: 'Invalid token format' });
    }

    try {
      // const decoded = jwt.verify(token, 'your-secret-key');
      // req.user = decoded;

      const result = await this.getMeApiMessage(token);
      this.logger.log('result', result);
      if (result.errorCode !== '0') {
        throw res.status(401).send({ message: 'Invalid or expired token' });
      }

      next();
    } catch (err) {
      return res.status(401).send({ message: 'Invalid or expired token' });
    }
  }
}
