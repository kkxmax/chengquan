//
//  HomeItemTableViewCell.h
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeItemTableViewCell : UITableViewCell

@property (nonatomic, weak) IBOutlet UIImageView *logoImageView;
@property (nonatomic, weak) IBOutlet UILabel *nameLabel;
@property (nonatomic, weak) IBOutlet UIButton *fenleiButton;
@property (nonatomic, weak) IBOutlet UILabel *commentTextView;

@end
