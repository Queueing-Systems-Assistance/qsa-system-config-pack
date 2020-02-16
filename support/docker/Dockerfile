FROM nginx:1.15

COPY ./build/config-pack.json /usr/share/nginx/html/

COPY ./support/docker/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
