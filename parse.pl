#!/usr/bin/env perl

use strict;
use warnings;

use Data::Dumper;
use HTML::Entities;

# mn properties:
# * just a message - ($msg1 =~ /<(.*?)>/) && ($msg2 =~ /(.*?)/)

# mne properties:
# * /me message - $msg1 =~ /\* (.+) (.+)/

# ml properties:
# * just left    - $msg1 =~ /(.+) вышел из конференции(: (.*))?$/ && ($msg2 eq "")
# * kicked       - $msg1 =~ /(.+) выгнали из конференции(: (.*))?$/ && ($msg2 eq "")
# * banned       - $msg1 =~ /(.+) запретили входить в конференцию(: (.*))?$/ && ($msg2 eq "")
# * changed nick - $msg1 =~ /(.+) сменил ник на (.*)?$/ && ($msg2 eq "")

# mj properties:
# * just joined - ($msg1 =~ /(.+) зашёл в конференцию$/) && ($msg2 eq "")

sub iterate_messages_in_file {
    my ($file_name, $action) = @_;
    open(my $fh, $file_name);
    while (<$fh>) {
        while ($_ =~ /<a.*?>\[(\d{2}:\d{2}:\d{2})\]<\/a> <font class="(.*?)">(.*?)<\/font>(.*?)<br\/>/g) {
            $action->(decode_entities($1),
                      decode_entities($2),
                      decode_entities($3),
                      decode_entities($4));
        }
    }
    close($fh);
}

sub count_types_in_file {
    my ($file_name, $types) = @_;
    my $message_type_mapping = {
        'mn' => 'Regular message',
        'mne' => '/me message',
        'mj' => 'Join message',
        'ml' => 'Leave message',
        'roomcsubject' => 'Subject'
    };
    iterate_messages_in_file $file_name, sub {
        my ($time, $raw_type, $msg1, $msg2) = @_;
        my $type = $message_type_mapping->{$raw_type};
        if (defined $types->{$type}) {
            $types->{$type}++;
        } else {
            $types->{$type} = 1;
        }
    }
}

sub extract_muc_and_date_from_file_name {
    my ($file_name) = @_;
    return $file_name =~ m/\/logs\/chat\/(.+?@.+?)\/(\d{4})\/(\d{2})\/(\d{2})\.html/;
}

sub extract_nickname_from_mn_message {
    my ($message) = @_;
    my ($nickname) = $message =~ m/<(.*?)>/;
    return $nickname;
}

sub main {
    my $nicknames = {};

    my $inc_nickname_counter = sub {
        my ($nickname) = @_;
        if (defined $nicknames->{$nickname}) {
            $nicknames->{$nickname}++;
        } else {
            $nicknames->{$nickname} = 1;
        }
    };

    foreach (@ARGV) {
        my $file_name = $_;
        my ($muc, $year, $month, $day) = extract_muc_and_date_from_file_name($file_name);

        iterate_messages_in_file $file_name, sub {
            my ($time, $type, $msg1, $msg2) = @_;
            if ($type eq 'mn') {
                my $nickname = extract_nickname_from_mn_message($msg1);
                $inc_nickname_counter->($nickname);
            }
        }
    }

    print Dumper($nicknames);
}

main();

1;
