# stage 1 building the code

FROM node:18-alpine AS base

FROM base AS builder

WORKDIR /app

COPY package.json yarn.lock ./

# RUN yarn install --frozen-lockfile
RUN yarn install

COPY . .

RUN yarn build


# stage 2

FROM base AS runner

WORKDIR /app  

COPY --from=builder /app/dist ./dist

COPY --from=builder /app/node_modules ./node_modules/

COPY --from=builder /app/package*.json ./

COPY .env .

EXPOSE 4000

CMD [ "node", "dist/main.js" ]
