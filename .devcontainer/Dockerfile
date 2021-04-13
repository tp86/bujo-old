FROM archlinux

RUN pacman --noconfirm -Syu base-devel

RUN pacman --noconfirm -S git jdk15-openjdk
RUN pacman --noconfirm -S sbt

RUN useradd -Um vscode
USER vscode
