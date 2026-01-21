import { Module, NestModule, MiddlewareConsumer } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
// import { AuthIgnoreMiddleware } from './auth_ignore.middleware';
import { AuthMiddleware } from './auth.middleware';
import { AttachmentsModule } from './attachments/attachments.module';
import { ImagesModule } from './images/images.module';
import { HttpModule } from '@nestjs/axios';

@Module({
  imports: [
    ConfigModule.forRoot(),
    AttachmentsModule,
    ImagesModule,
    HttpModule,
  ],
  controllers: [],
  providers: [],
})
export class AppModule implements NestModule {
  configure(consumer: MiddlewareConsumer) {
    consumer.apply(AuthMiddleware).forRoutes('*');
  }
}
