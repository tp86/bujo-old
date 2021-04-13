FROM archlinux

RUN patched_glibc=glibc-linux4-2.33-4-x86_64.pkg.tar.zst && \
    curl -LO "https://repo.archlinuxcn.org/x86_64/$patched_glibc" && \
    bsdtar -C / -xvf "$patched_glibc"
RUN pacman --noconfirm -Syu --ignore glibc base-devel

RUN pacman --noconfirm -S git openjdk-src
RUN pacman --noconfirm -S sbt

RUN useradd -Um vscode
USER vscode
