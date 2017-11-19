//
//  HotDetailPersonalCell.m
//  chengxin
//
//  Created by seniorcoder on 10/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HotDetailPersonalCell.h"
#import "Global.h"

@implementation HotDetailPersonalCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)onClickMoreReplyButton:(id)sender {
    self.moreReplyButton.selected = !self.moreReplyButton.selected;
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_MORE_REPLY_VIEW_NOTIFICATION object:nil];
}


@end
