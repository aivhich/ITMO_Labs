mkdir lab0
cd lab0
mkdir bronzong5
cd bronzong5
mkdir turtwig
mkdir sunflora
mkdir venipede
touch infernape
echo "Способности Ember Taunt Mach Punch Fury Swipes"> infernape
echo "Flame Wheel Feint Punishment Close Combat Fire Spin Acrobatics Calm Mind Flare" >> infernape
echo "Blitz" >> infernape

touch magnemite
echo "satk 10 sdef=6 spd=5">magnemite

mkdir slowpoke
cd -

mkdir charizard0
cd charizard0
mkdir nidoking
mkdir larvitar

touch wobbuffet
echo "satk=3 sdef=6" >> wobbuffet
echo "spd=3" >> wobbuffet

touch treecko
echo "Ходы Body Slam Counter Double-Edge Drain Punch" > treecko
echo "Dynamicpunch Endeavor Focus Punch Fury Cutter Giga Drain Grass Pledge" >> treecko
echo "Iron Tail Low Kick Mega Kick Mega Punch Mud-Slap Secret Power Seed" >> treecko
echo "Bomb Seismic Toss Sleep Talk Snore Swift Synthesis Thunderpunch Worry" >> treecko
echo "Seed" >> treecko

touch lairon
echo "Тип покемона STEEL ROCK" > lairon

touch golduck
echo "satk=10 sdef=8" > golduck
echo "spd=9" >> golduck
cd -

mkdir haunter5
cd haunter5
mkdir larvesta
mkdir oshawott
mkdir vanilluxe
mkdir crawdaunt
cd -
touch kingdra3
echo "Тип диеты Herbivore" > kingdra3

touch meganium2
echo "Способности Growl" > meganium2
echo "Razor Leaf Poisonpowder Synthesis Reflect Magical Leaf Natural Gift" >>meganium2
echo "Petal Dance Sweet Scent Light Screen Body Slam Safeguard Aromatherapy" >> meganium2
echo "Solarbeam">>meganium2


touch porygon26
echo "satk=11 sdef=10 spd=6" > porygon26

cd lab0
chmod 752 bronzong5
chmod 335 bronzong5/turtwig
chmod u=rwx,g=wx,o=rw bronzong5/sunflora
chmod u=rwx,g=wx,o=wx bronzong5/venipede
chmod 064 bronzong5/infernape
chmod 064 bronzong5/magnemite
chmod u=wx,g=rw,o=x bronzong5/slowpoke
chmod u=rwx,g=rwx,o=rwx charizard0
chmod u=wx,g=x,o=w charizard0/nidoking
chmod u=rwx,g=rwx,o=rwx charizard0/larvitar
chmod 664 charizard0/wobbuffet
chmod 624 charizard0/treecko
chmod 006 charizard0/lairon
chmod 044 charizard0/golduck
chmod 751 haunter5
chmod 511 haunter5/larvesta
chmod 524 haunter5/oshawott
chmod 363 haunter5/vanilluxe
chmod u=rwx,g=rx,o=w haunter5/crawdaunt
chmod 444 kingdra3
chmod u=rw,g=,o=r meganium2
chmod 400 porygon26
cd lab0
ln meganium2 charizard0/golduckmeganium
chmod u=rwx charizard0/lairon
chmod u=rwx charizard0/nidoking
chmod u=rwx charizard0/golduck
chmod u=rwx haunter5/oshawott
cat charizard0/lairon charizard0/treecko > porygon26_21
cp -r charizard0 haunter5/oshawott
chmod 006 charizard0/lairon 
chmod 524 haunter5/oshawott
chmod 044 charizard0/golduck
chmod u=wx charizard0/nidoking
ln -s kingdra3 charizard0/treeckokingdra
ln -s Copy_58 bronzong5
cat kingdra3 > charizard0/treeckokingdra
cp meganium2 haunter5/vanilluxe
echo "ЗАДАНИЕ 1: Рекурсивно подсчитать количество строк содержимого файлов из директории lab0, "
echo "имя которых начинается на 'l', отсортировать вывод по увеличению количества, подавить вывод ошибок доступа"
grep -r -c "^" lab0 --include="*/l*" 2>/dev/null | sort -t: -k2,2n
echo

echo "ЗАДАНИЕ 2: Вывести рекурсивно список имен и атрибутов файлов в директории lab0, "
echo "заканчивающихся на символ 't', список отсортировать по имени z->a, подавить вывод ошибок доступа"
ls -lR lab0 2>/dev/null | grep "^-" | grep -E "t$" | sort -k9r
echo

echo "ЗАДАНИЕ 3: Рекурсивно вывести содержимое файлов с номерами строк из директории lab0,"
echo "имя которых заканчивается на 'e', строки отсортировать по имени a->z, добавить вывод ошибок доступа в стандартный поток вывода"
grep -r -n "e$" lab0  2>&1 | sort -t: -k1,1
echo

echo "ЗАДАНИЕ 4: Вывести список имен файлов в директории charizard0, "
echo "список отсортировать по имени a->z, ошибки доступа не подавлять и не перенаправлять"
ls -1 lab0/charizard0 | sort
echo

echo "ЗАДАНИЕ 5: Вывести четыре последних элемента рекурсивного списка имен и атрибутов файлов"
echo " в директории lab0, список отсортировать по возрастанию количества жестких ссылок, ошибки доступа не подавлять и не перенаправлять"
ls -lR lab0 | grep "^-" | sort -k2,2n | tail -n 4
echo

echo "ЗАДАНИЕ 6: Вывести два последних элемента рекурсивного списка имен и атрибутов файлов"
echo "в директории lab0, содержащих строку la, список отсортировать по убыванию количества жестких ссылок, подавить вывод ошибок доступа"
ls -lR lab0 2>/dev/null | grep "^-" | grep "la" | sort -k2,2nr | tail -n 2
cd lab0
rm meganium2
rm charizard0/treecko
rm charizard0/treeckokingd*
rm charizard0/golduckmegani*
chmod u=rwx charizard0 charizard0/* bronzong5/turtwig
rm -rf charizard0
rm -rf bronzong5/turtwig

