#! /bin/bash
## Description: Configures Media Server automatically.
## Author     : Henrique Rosa

readonly BASEDIR=$(cd $(dirname "${BASH_SOURCE[0]}") && pwd)

echo 'Media Server automatic configuration started:'
for f in $BASEDIR/autoconfig.d/*.sh; do
    source $f
done
echo 'Media Server automatic configuration finished!'