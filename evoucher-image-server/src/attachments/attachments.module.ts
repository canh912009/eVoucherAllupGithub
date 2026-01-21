import { Module } from '@nestjs/common';
import { HttpModule } from '@nestjs/axios';
import { AttachmentsService } from './attachments.service';
import { AttachmentsController } from './attachments.controller';
import { UploadsService } from "../uploads.service";

@Module({
  controllers: [AttachmentsController],
  providers: [AttachmentsService, UploadsService],
  imports: [HttpModule],
})
export class AttachmentsModule {}
