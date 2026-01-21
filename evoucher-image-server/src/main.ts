import 'dotenv/config';
import { ValidationPipe } from '@nestjs/common';
import { NestFactory } from '@nestjs/core';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';
import { AppModule } from './app.module';
import { NestExpressApplication } from '@nestjs/platform-express';
import * as express from 'express';
// import { join } from 'path';
import { LoggerService } from './logger.service';

async function bootstrap() {
  const app = await NestFactory.create<NestExpressApplication>(AppModule, {
    cors: true,
    logger: new LoggerService(),
  });

  /**
   * Configure endpoint API. Ex: http://localhost:4000/api
   */
  app.setGlobalPrefix('api');
  app.useGlobalPipes(new ValidationPipe());

  /**
   * Configure docs Swagger. Ex: http://localhost:4000/docs
   */
  const config = new DocumentBuilder()
    .setTitle('Evoucher File Api')
    .setDescription(
      'Evoucher File Api: upload and download files, public images',
    )
    .setVersion('1.0')
    .addTag('evoucher-fileapi')
    .build();
  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('docs', app, document);

  /**
   * For public assets. Ex: http://localhost:4000/static
   * Access: protocol://ip:port/static/[path-asset-returned]. Ex: http://localhost:4000/static/images/abc.jpg
   */
  const public_folder = process.env.PUBLIC_FOLDER_UPLOAD;
  console.log('public_folder', public_folder);

  // For public images
  // app.use(
  //   '/static',
  //   express.static(join(__dirname, '..', '../uploads/public')),
  // );
  // app.use(
  //   '/static',
  //   express.static(join(__dirname, '..', '../../../uploads2/public')),
  // );
  app.use('/static', express.static(public_folder));

  const PORT = process.env.PORT || 4000;
  await app.listen(PORT);
  console.log(`Application is running on: ${await app.getUrl()}/api`);
}
bootstrap();
