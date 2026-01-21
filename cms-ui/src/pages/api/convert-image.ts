import axios from 'axios';
import { NextApiRequest, NextApiResponse } from 'next';

export default async (req: NextApiRequest, res: NextApiResponse) => {
  const imageUrl = req.query.url as string;

  try {
    const response = await axios.get(imageUrl, { responseType: 'arraybuffer' });
    const imageBuffer = Buffer.from(response.data, 'binary');
    const dataUrl = `data:${
      response.headers['content-type']
    };base64,${imageBuffer.toString('base64')}`;

    // console.log('dataUrl', dataUrl);
    res.status(200).json({ dataUrl });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'An error occurred' });
  }
};
