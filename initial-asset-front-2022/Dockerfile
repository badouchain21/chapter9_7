FROM nginx:1.16.1
VOLUME /tmp
ADD zydb_front nginx_project.conf /opt/zydb_front/
RUN rm -f /etc/nginx/conf.d/default.conf && mv /opt/zydb_front/nginx_project.conf /etc/nginx/conf.d/

