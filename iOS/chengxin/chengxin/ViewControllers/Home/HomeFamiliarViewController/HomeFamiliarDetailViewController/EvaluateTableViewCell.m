//
//  EvaluateTableViewCell.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "EvaluateTableViewCell.h"
#import "Global.h"

@implementation EvaluateTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)onReplyMoreSelected:(id)sender {
    self.moreReplyButton.selected = !self.moreReplyButton.selected;
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_MORE_REPLY_VIEW_NOTIFICATION object:nil];
}

@end
